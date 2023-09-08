package com.project.hangmani.user.repository;

import com.project.hangmani.config.PropertiesValues;
import com.project.hangmani.exception.FailDeleteData;
import com.project.hangmani.exception.FailInsertUser;
import com.project.hangmani.security.AES;
import com.project.hangmani.user.model.dto.UserDTO;
import com.project.hangmani.user.model.entity.User;
import com.project.hangmani.util.ConvertData;
import com.project.hangmani.util.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static com.project.hangmani.config.query.UserQueryConst.*;

@Repository
@Slf4j
public class UserRepositoryImpl implements UserRepository{
    private JdbcTemplate template;
    private Util util;
    private final AES aes;
    private ConvertData convertData;
    @Autowired
    public UserRepositoryImpl(DataSource dataSource, AES aes, PropertiesValues propertiesValues) {
        this.template = new JdbcTemplate(dataSource);
        this.util = new Util(propertiesValues);
        this.aes = aes;
        this.convertData = new ConvertData(propertiesValues);
    }

    public Optional<UserDTO> getByoAuthId(String oAuthId) {
        //check base64 format
        if (!this.util.isBase64(oAuthId)) {
            byte[] bytes = this.aes.encryptData(oAuthId, StandardCharsets.UTF_8);
            oAuthId = this.convertData.byteToBase64(bytes);
        }
        List<UserDTO> user = template.query(getUserByOAuthIdSql, userRowMapper(), oAuthId);
        return user.stream().findAny();
    }

    public Optional<UserDTO> getById(String userID) {
        List<UserDTO> user = template.query(getUserByIDSql, userRowMapper(), userID);

        return user.stream().findAny();
    }

    public String add(User user) {
        String uuid = getUuid();
        user.setId(uuid);
        int ret = template.update(insertUserSql, new Object[]{user.getEmail(),
                user.getAge(), user.getGender(), user.getId()});
        ret += template.update(insertOAuthSql, new Object[]{
                user.getId(), user.getOAuthType(), user.getOAuthID()});
        if (ret != 2)
            throw new FailInsertUser();
        return uuid;
    }
    private RowMapper<UserDTO> userRowMapper() {
        return (rs, rowNum) -> {
            UserDTO user = new UserDTO();
            user.setId(rs.getString("id"));
            user.setEmail(rs.getString("email"));
            user.setGender(rs.getString("gender"));
            user.setAge(rs.getString("age"));
            user.setOAuthType(rs.getString("oauthtype"));
            user.setOAuthID(rs.getString("oauthid"));
            user.setRefreshToken("refreshToken");
            return user;
        };
    }
    public int delete(String userID) {
        int ret = template.update(deleteOAuthSql, new Object[]{userID});
        ret += template.update(deleteUserSql, new Object[]{userID});
        if (ret != 2)
            throw new FailDeleteData();
        return ret;
    }
    private String getUuid() {
        return this.util.getUUID().toString();
    }
}

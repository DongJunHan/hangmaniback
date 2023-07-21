package com.project.hangmani.repository;

import com.project.hangmani.domain.User;
import com.project.hangmani.dto.UserDTO.RequestInsertUserDTO;
import com.project.hangmani.exception.FailDeleteData;
import com.project.hangmani.exception.FailInsertUser;
import com.project.hangmani.security.AES;
import com.project.hangmani.util.ConvertData;
import com.project.hangmani.util.Util;
import lombok.extern.slf4j.Slf4j;
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
public class UserRepository {
    private JdbcTemplate template;
    private Util util;
    private AES aes;
    private ConvertData convertData;
    public UserRepository(DataSource dataSource, Util util,AES aes) {
        this.template = new JdbcTemplate(dataSource);
        this.util = util;
        this.aes = aes;
        this.convertData = new ConvertData();
    }

    public Optional<User> findByoAuthId(String oAuthId) {
        //check base64 format
        if (!this.util.isBase64(oAuthId)) {
            byte[] bytes = this.aes.encryptData(oAuthId, StandardCharsets.UTF_8);
            oAuthId = this.convertData.byteToBase64(bytes);
        }
        List<User> user = template.query(getUserByOAuthIdSql, userRowMapper(), oAuthId);
        return user.stream().findAny();
    }

    public Optional<User> findById(String userID) {
        List<User> user = template.query(getUserByIDSql, userRowMapper(), userID);
        log.info("result: {}", user);
        return user.stream().findAny();
    }

    public String insertUser(RequestInsertUserDTO requestDTO) {
        String userID = this.util.getUUID().toString();
        requestDTO.setId(userID);

        String base64OAuthID = requestDTO.getOAuthID();
        if (!this.util.isBase64(requestDTO.getOAuthID())) {
            //encrypt, encoding oauth ID
            byte[] encryptOAuthID = this.aes.encryptData(requestDTO.getOAuthID(), StandardCharsets.UTF_8);
            base64OAuthID = this.convertData.byteToBase64(encryptOAuthID);
        }
        //set oauth id
        requestDTO.setOAuthID(base64OAuthID);

        //encrypt, encoding refresh token data
        byte[] encryptToken = this.aes.encryptData(requestDTO.getRefreshToken(), StandardCharsets.UTF_8);
        String base64Token = this.convertData.byteToBase64(encryptToken);
        //set refresh token
        requestDTO.setRefreshToken(base64Token);
        int ret = template.update(insertUserSql, new Object[]{ requestDTO.getEmail(),
                requestDTO.getAge(), requestDTO.getGender(), requestDTO.getId()});
        ret += template.update(insertOAuthSql, new Object[]{
                requestDTO.getId(), requestDTO.getOAuthType(), requestDTO.getOAuthID()});
        if (ret != 2)
            throw new FailInsertUser();
        return userID;
    }
    private RowMapper<User> userRowMapper() {
        return (rs, rowNum) -> {
            User user = new User();
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
    public int deleteUserById(String userID) {
        int ret = template.update(deleteOAuthSql, new Object[]{userID});
        ret += template.update(deleteUserSql, new Object[]{userID});
        if (ret != 2)
            throw new FailDeleteData();
        return ret;
    }
}

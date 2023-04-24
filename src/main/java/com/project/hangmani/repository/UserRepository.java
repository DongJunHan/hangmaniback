package com.project.hangmani.repository;

import com.project.hangmani.domain.User;
import com.project.hangmani.dto.UserDTO.RequestInsertUserDTO;
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

//    private final String insertUserSql = "insert into user(REFRESHTOKEN, REFRESHTOKENEXPIRE, id) values(?,?,?);";
//    private final String insertScopeSql = "insert into scope(id, email, age, gender) values(?,?,?,?);";
//    private final String insertOAuthSql = "insert into oauth(id, oauthtype) values(?,?);";
    private final String updateRefreshTokenSql = "update user set from age=?, email=?, gender=? where id=?";
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
        return user.stream().findAny();
    }

    public String insertUser(RequestInsertUserDTO requestDTO) {
        String userID = this.util.getUUID().toString();
        requestDTO.setId(userID);
        //encrypt, encoding oauth ID
        byte[] encryptOAuthID = this.aes.encryptData(requestDTO.getOAuthID(), StandardCharsets.UTF_8);
        String base64OAuthID = this.convertData.byteToBase64(encryptOAuthID);
        //set oauth id
        requestDTO.setOAuthID(base64OAuthID);

        //encrypt, encoding refresh token data
        byte[] encryptToken = this.aes.encryptData(requestDTO.getRefreshToken(), StandardCharsets.UTF_8);
        String base64Token = this.convertData.byteToBase64(encryptToken);
        //set refresh token
        requestDTO.setRefreshToken(base64Token);

        int ret = template.update(insertUserSql, new Object[]{ requestDTO.getEmail(),
                requestDTO.getAge(), requestDTO.getGender(), requestDTO.getId(),
                requestDTO.getId(), requestDTO.getOAuthType(), requestDTO.getOAuthID()});
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
            return user;
        };
    }

//    public int insertScope(RequestInsertScopeDTO requestDTO) {
//        return template.update(insertScopeSql, new Object[]{requestDTO.getId(),
//                requestDTO.getEmail(), requestDTO.getAge(), requestDTO.getGender()});
//    }
//
//    public int insertOAuthType(RequestInsertOAuthDTO requestDTO) {
//        return template.update(insertOAuthSql, new Object[]{requestDTO.getId(), requestDTO.getOAuthType()});
//    }

    public int deleteUser(String userID) {
        return template.update(deleteUserSql, new Object[]{userID, userID});
    }
}

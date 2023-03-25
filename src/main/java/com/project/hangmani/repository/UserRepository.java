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
import org.springframework.validation.ObjectError;

import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.stream;

@Repository
@Slf4j
public class UserRepository {
    private final String findByOAuthIdSql = "SELECT \n" +
            "    member.ID, oauth.oauthid, oauth.oauthtype, scope.email, scope.age, scope.gender, member.REFRESHTOKEN, member.REFRESHTOKENEXPIRE\n" +
            "FROM \n" +
            "    oauth\n" +
            "    JOIN scope ON oauth.ID = scope.ID\n" +
            "    JOIN member ON oauth.ID = member.ID\n" +
            "WHERE\n" +
            "  oauth.OAUTHID=?;";

    private final String findByIDSql = "SELECT \n" +
            "    member.ID, oauth.oauthid, oauth.oauthtype, scope.email, scope.age, scope.gender, member.REFRESHTOKEN, member.REFRESHTOKENEXPIRE\n" +
            "FROM \n" +
            "    oauth\n" +
            "    JOIN scope ON oauth.ID = scope.ID\n" +
            "    JOIN member ON oauth.ID = member.ID\n" +
            "WHERE\n" +
            "  member.id=?;";
    private final String deleteSql = "DELETE FROM oauth WHERE ID = ?; DELETE FROM scope WHERE ID = ?; DELETE FROM member WHERE ID = ?;";
    private final String insertUserSql = "insert into member(REFRESHTOKEN, REFRESHTOKENEXPIRE, id) values(?,?,?);\n"+
                                         "insert into scope(id, email, age, gender) values(?,?,?,?);\n"+
                                         "insert into oauth(id, oauthtype, oauthID) values(?,?,?);";
//    private final String insertUserSql = "insert into member(REFRESHTOKEN, REFRESHTOKENEXPIRE, id) values(?,?,?);";
//    private final String insertScopeSql = "insert into scope(id, email, age, gender) values(?,?,?,?);";
//    private final String insertOAuthSql = "insert into oauth(id, oauthtype) values(?,?);";
    private final String updateRefreshTokenSql = "update member set from refreshtoken=?, REFRESHTOKENEXPIRE=? where id=?";
    private JdbcTemplate template;
    private Util util;
    private AES aes;
    private ConvertData convertData;
    public UserRepository(DataSource dataSource, Util util) {
        this.template = new JdbcTemplate(dataSource);
        this.util = util;
        this.aes = new AES(this.util);
        this.convertData = new ConvertData();
    }

    public Optional<User> findByoAuthId(String oAuthId) {
        //check base64 format
        if (!this.util.isBase64(oAuthId)) {
            byte[] bytes = this.aes.encryptData(oAuthId, StandardCharsets.UTF_8);
            oAuthId = this.convertData.byteToBase64(bytes);
        }
        List<User> user = template.query(findByOAuthIdSql, userRowMapper(), oAuthId);
        return user.stream().findAny();
    }

    public Optional<User> findById(String userID) {
        List<User> user = template.query(findByIDSql, userRowMapper(), userID);
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

        int ret = template.update(insertUserSql, new Object[]{requestDTO.getRefreshToken(),
                requestDTO.getRefreshTokenExpire(), requestDTO.getId(),
                requestDTO.getId(), requestDTO.getEmail(), requestDTO.getAge(), requestDTO.getGender(),
                requestDTO.getId(), requestDTO.getOAuthType(), requestDTO.getOAuthID()});
        return userID;
    }
    private RowMapper<User> userRowMapper() {
        return (rs, rowNum) -> {
            User member = new User();
            member.setId(rs.getString("id"));
            member.setEmail(rs.getString("email"));
            member.setGender(rs.getString("gender"));
            member.setAge(rs.getString("age"));
            member.setRefreshToken(rs.getString("refreshToken"));
            member.setRefreshTokenExpire(rs.getDate("REFRESHTOKENEXPIRE"));
            member.setOAuthType(rs.getString("oauthtype"));
            member.setOAuthID(rs.getString("oauthid"));
            return member;
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
        log.info("id={}", userID);
//        List<Object[]> batchArgs = new ArrayList<>();
//        batchArgs.add(new Object[]{userID, userID, userID});
//        int[] ints = template.batchUpdate(deleteSql, batchArgs);
//        log.info("ret={}",ints);
        return template.update(deleteSql, new Object[]{userID, userID, userID});
    }


    public int updateRefreshToken(String refreshToken, Date expiresIn, String userID) {
        return template.update(updateRefreshTokenSql, new Object[]{refreshToken, expiresIn, userID});
    }
}

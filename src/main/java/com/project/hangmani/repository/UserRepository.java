package com.project.hangmani.repository;

import com.project.hangmani.domain.User;
import com.project.hangmani.dto.UserDTO.RequestInsertOAuthDTO;
import com.project.hangmani.dto.UserDTO.RequestInsertScopeDTO;
import com.project.hangmani.dto.UserDTO.RequestInsertUserDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {
    private final String findByIdSql = "SELECT \n" +
            "    oauth.ID, oauth.oauthtype, scope.email, scope.age, scope.gender, member.REFRESHTOKEN, member.REFRESHTOKENEXPIRE\n" +
            "FROM \n" +
            "    oauth\n" +
            "    JOIN scope ON oauth.ID = scope.ID\n" +
            "    JOIN member ON oauth.ID = member.ID\n" +
            "WHERE\n" +
            "  member.ID=?;";
    private final String insertSql = "insert into member(refreshtoken,REFRESHTOKENEXPIRE,ID) values(?,?,?);";
    private final String insertScopeSql = "insert into scope(id, email, age, gender) values(?,?,?,?);";
    private final String insertOAuthSql = "insert into oauth(id, oauthtype) values(?,?);";
    private JdbcTemplate template;
    public UserRepository(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    public Optional<User> findById(String id) {
        List<User> user = template.query(findByIdSql, userRowMapper(), id);
        return user.stream().findAny();
    }

    public int insertUser(RequestInsertUserDTO requestDTO) {
        return template.update(insertSql,new Object[]{requestDTO.getRefreshToken(),
                requestDTO.getRefreshTokenExpire(), requestDTO.getId()});
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
            return member;
        };
    }

    public int insertScope(RequestInsertScopeDTO requestDTO) {
        return template.update(insertScopeSql, new Object[]{requestDTO.getId(),
                requestDTO.getEmail(), requestDTO.getAge(), requestDTO.getGender()});
    }

    public int insertOAuthType(RequestInsertOAuthDTO requestDTO) {
        return template.update(insertOAuthSql, new Object[]{requestDTO.getId(), requestDTO.getOAuthType()});
    }
}

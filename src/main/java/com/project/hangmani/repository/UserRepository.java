package com.project.hangmani.repository;

import com.project.hangmani.domain.Store;
import com.project.hangmani.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {
    private final String findByIdSql = "select * from member where member_id=?";
    private JdbcTemplate template;
    public UserRepository(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    public Optional<User> findById(String id) {
        List<User> user = template.query(findByIdSql, userRowMapper(), id);
        return user.stream().findAny();
    }
    private RowMapper<User> userRowMapper() {
        return (rs, rowNum) -> {
            User member = new User();
            member.setMember_id(rs.getString("member_id"));
            member.setMoney(rs.getInt("money"));
            return member;
        };
    }
}

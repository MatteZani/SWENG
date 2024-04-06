package com.sweng.mapper;

import com.sweng.entity.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User(rs.getInt("ID"), rs.getString("USERNAME"), rs.getString("PASSWORD"));

        return user;
    }
}

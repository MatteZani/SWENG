package com.sweng.mapper;

import com.sweng.entity.Riddle;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RiddleRowMapper implements RowMapper<Riddle> {

    @Override
    public Riddle mapRow(ResultSet rs, int rowNum) throws SQLException {
        Riddle riddle = new Riddle(rs.getInt("ID"), rs.getString("DOMANDA"), rs.getString("RISPOSTA"));

        return riddle;
    }
}

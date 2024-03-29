package com.sweng.mapper;

import com.sweng.entity.StoryObject;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StoryObjectRowMapper implements RowMapper<StoryObject> {

    @Override
    public StoryObject mapRow(ResultSet rs, int rowNum) throws SQLException {

        StoryObject storyObject = new StoryObject(rs.getInt("ID"), rs.getString("NOME"), rs.getString("DESCRIZIONE"), rs.getInt("ID_STORIA"));

        return storyObject;
    }
}

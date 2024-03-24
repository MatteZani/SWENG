package com.sweng.mapper;

import com.sweng.entity.Story;
import com.sweng.entity.StoryBuilder;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StoryRowMapper implements RowMapper<Story> {
    @Override
    public Story mapRow(ResultSet rs, int rowNum) throws SQLException {
        Story story = new StoryBuilder().
                setId(rs.getInt("ID"))
                .setTitle(rs.getString("TITLE"))
                .setPlot(rs.getString("PLOT"))
                .setInitialScenario(rs.getInt("INITIAL_SCENARIO"))
                .setCategory(rs.getString("CATEGORY"))
                .setCreator(rs.getString("CREATOR"))
                .build();

        // Imposta altri attributi se necessario
        return story;
    }
}
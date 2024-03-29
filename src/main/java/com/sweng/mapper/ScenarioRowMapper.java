package com.sweng.mapper;

import com.sweng.entity.Scenario;
import com.sweng.entity.ScenarioBuilder;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ScenarioRowMapper implements RowMapper<Scenario> {

    @Override
    public Scenario mapRow(ResultSet rs, int rowNum) throws SQLException {
        Scenario scenario = new ScenarioBuilder().setId(rs.getInt("ID"))
                .setDescription(rs.getString("DESCRIZIONE")).setStoryId(rs.getInt("ID_STORIA"))
                .setNecessaryObjectId(rs.getInt("ID_OGGETTO_NECESSARIO")).setFoundObjectId(rs.getInt("ID_OGGETTO_OTTENUTO"))
                .setRiddleId(rs.getInt("ID_INDOVINELLO")).build();

        return scenario;

    }
}

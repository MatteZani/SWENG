package com.sweng;


import com.sweng.entity.Riddle;
import com.sweng.entity.Scenario;
import com.sweng.entity.Story;
import com.sweng.entity.StoryObject;
import com.sweng.mapper.RiddleRowMapper;
import com.sweng.mapper.ScenarioRowMapper;
import com.sweng.mapper.StoryObjectRowMapper;
import com.sweng.mapper.StoryRowMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class RowMapperTests {

    @Mock
    private ResultSet resultSet;

    @Test
    void storyRowMapperTest() throws SQLException {
        //given
        StoryRowMapper storyRowMapper = new StoryRowMapper();

        //when
        when(resultSet.getInt("ID")).thenReturn(1);
        when(resultSet.getString("TITLE")).thenReturn("Titolo della storia");
        when(resultSet.getString("PLOT")).thenReturn("Trama della storia");
        when(resultSet.getString("CATEGORY")).thenReturn("Categoria della storia");
        when(resultSet.getString("CREATOR")).thenReturn("Creatore della storia");
        when(resultSet.getInt("INITIAL_SCENARIO")).thenReturn(1);

        Story story = storyRowMapper.mapRow(resultSet, 0);

        //then
        assertEquals(1, story.getId());
        assertEquals("Titolo della storia", story.getTitle());
        assertEquals("Trama della storia", story.getPlot());
        assertEquals("Categoria della storia", story.getCategory());
        assertEquals("Creatore della storia", story.getCreator());
        assertEquals(1, story.getInitialScenario());
    }

    @Test
    void scenarioRowMapperTest() throws SQLException {
        //given
        ScenarioRowMapper scenarioRowMapper = new ScenarioRowMapper();

        //when
        when(resultSet.getInt("ID")).thenReturn(1);
        when(resultSet.getString("DESCRIZIONE")).thenReturn("Descrizione dello scenario");
        when(resultSet.getInt("ID_STORIA")).thenReturn(1);
        when(resultSet.getInt("ID_OGGETTO_NECESSARIO")).thenReturn(1);
        when(resultSet.getInt("ID_OGGETTO_OTTENUTO")).thenReturn(1);
        when(resultSet.getInt("ID_INDOVINELLO")).thenReturn(1);

        Scenario scenario = scenarioRowMapper.mapRow(resultSet, 0);

        //then
        assertEquals(1, scenario.getId());
        assertEquals("Descrizione dello scenario", scenario.getDescription());
        assertEquals(1, scenario.getStoryId());
        assertEquals(1, scenario.getNecessaryObjectId());
        assertEquals(1, scenario.getFoundObjectId());
        assertEquals(1, scenario.getRiddleId());

    }

    @Test
    void storyObjectRowMapperTest() throws SQLException {
        //given
        StoryObjectRowMapper storyObjectRowMapper = new StoryObjectRowMapper();

        //when

        when(resultSet.getInt("ID")).thenReturn(1);
        when(resultSet.getString("NOME")).thenReturn("Nome dell'oggetto");
        when(resultSet.getString("DESCRIZIONE")).thenReturn("Descrizione dell'oggetto");
        when(resultSet.getInt("ID_STORIA")).thenReturn(1);

        StoryObject storyObject = storyObjectRowMapper.mapRow(resultSet, 0);

        //then
        assertEquals(1, storyObject.getId());
        assertEquals("Nome dell'oggetto", storyObject.getName());
        assertEquals("Descrizione dell'oggetto", storyObject.getDescription());
        assertEquals(1, storyObject.getStoryId());
    }

    @Test
    void riddleRowMapper() throws SQLException {

        //given
        RiddleRowMapper riddleRowMapper = new RiddleRowMapper();

        //when
        when(resultSet.getInt("ID")).thenReturn(1);
        when(resultSet.getString("DOMANDA")).thenReturn("Domanda dell'indovinello");
        when(resultSet.getString("RISPOSTA")).thenReturn("Risposta dell'indovinello");

        Riddle riddle = riddleRowMapper.mapRow(resultSet, 0);
        //then
        assertEquals(1, riddle.getId());
        assertEquals("Domanda dell'indovinello", riddle.getQuestion());
        assertEquals("Risposta dell'indovinello", riddle.getAnswer());
    }
}

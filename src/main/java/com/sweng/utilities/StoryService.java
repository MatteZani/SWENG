package com.sweng.utilities;

import com.sweng.entity.Story;
import com.sweng.mapper.StoryRowMapper;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class StoryService {

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    Logger logger = LoggerFactory.getLogger(StoryService.class);

    public void createStory(Story story) {
        try {
            String sql = "INSERT INTO STORIE(TITLE, PLOT, CATEGORY, CREATOR, INITIAL_SCENARIO) VALUES (?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql, story.getTitle(), story.getPlot(), story.getCategory(), story.getCreator(), null);


        } catch (DataAccessException e) {
            logger.error("Lanciata eccezione nel metodo createStory della classe DBHandler. Causa dell'eccezione: {}. Descrizione dell'eccezione: {}", e.getCause(), e.getMessage());
            throw e;
        }
    }
    public List<Story> getStories() {
        try {
            return jdbcTemplate.query("SELECT * FROM STORIE", new StoryRowMapper());

        } catch (DataAccessException e) {
            logger.error("Lanciata eccezione nel metodo getStories della classe StoryService. Causa dell'eccezione: {}. Descrizione dell'eccezione: {}", e.getCause(), e.getMessage());
            return null;

        }
    }

    public List<Integer> getSavedIdStories(String username) { //
        try {
            List<Integer> savedIdStories = jdbcTemplate.queryForList("SELECT ID_STORIA FROM PARTITE WHERE USERNAME = ?", Integer.class, username);
            return savedIdStories;
            //return jdbcTemplate.query("SELECT * FROM PARTITE WHERE USERNAME = ?",  new StoryRowMapper(), username);
        } catch (DataAccessException e) {
            logger.error("Errore nel metodo getSavedStories della classe StoryService. Causa: {}. Messaggio: {}", e.getCause(), e.getMessage());
            return Collections.emptyList(); // Restituisci una lista vuota in caso di errore o se non ci sono storie salvate
        }
    }

    public List<Story> getStoriesByCreator(String username){
        try {
            return jdbcTemplate.query("SELECT * FROM STORIE WHERE CREATOR = ?", new StoryRowMapper(), username);

        } catch (DataAccessException e) {
            logger.error("Lanciata eccezione nel metodo getStoriesByCreator della classe StoryService.. Causa dell'eccezione: {}. Descrizione dell'eccezione: {}", e.getCause(), e.getMessage());
            return null;
        }

    }

    public Story getStoryById(int storyId) {
        String sql = "SELECT * FROM STORIE WHERE ID = ?";
        Story story = jdbcTemplate.queryForObject(sql, new StoryRowMapper(), storyId);

        return story;

    }

    public int getScenariosNumberByStoryId(int storyId){
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM SCENARI WHERE ID_STORIA = ?", Integer.class, storyId);
    }

    public int getMaxStoryId(){
        try {
            String sql = "SELECT MAX(ID) FROM STORIE";
            int maxStoryId = jdbcTemplate.queryForObject(sql, Integer.class);

            return maxStoryId;
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        } catch (NullPointerException e) {
            logger.error("NullPointerException nel metodo getMaxStoryId della classe StoryService");
            return 0;
        }
    }

    public List<Map<String, Object>> getLinksByStoryId(int storyId) {
        try {
            return jdbcTemplate.queryForList("SELECT * FROM COLLEGAMENTI WHERE STORIA_APPARTENENZA = ?", storyId);

        } catch (DataAccessException e) {
            logger.error("Lanciata eccezione nel metodo getLinksByStoryId della classe DBHandler. Causa dell'" +
                    "eccezione: {}. Descrizione dell'eccezione: {}", e.getCause(), e.getMessage());
            return null;
        }
    }

    public int getStoryIdByScenarioId(int scenarioId) {
        try {
            String sql = "SELECT ID_STORIA FROM SCENARI WHERE ID = ?";
            return jdbcTemplate.queryForObject(sql, Integer.class, scenarioId);
        } catch (DataAccessException e) {
            logger.error("Impossibile recuperare l'ID della storia per lo scenario con ID: {}. Errore: {}", scenarioId, e.getMessage());
            return 0;
        }
    }


    public List<Story> findStoriesByFilter(String title, String category, String creator) {
        List<Object> params = new ArrayList<>();
        String sql = "SELECT * FROM STORIE WHERE 1=1";

        if (title != null && !title.trim().isEmpty()) {
            sql += " AND TITLE LIKE ?";
            params.add("%" + title + "%");
        }
        if (category != null && !category.trim().isEmpty()) {
            sql += " AND CATEGORY = ?";
            params.add(category);
        }
        if (creator != null && !creator.trim().isEmpty()) {
            sql += " AND CREATOR = ?";
            params.add(creator);
        }

        return jdbcTemplate.query(sql, new StoryRowMapper(), params.toArray());
    }

}
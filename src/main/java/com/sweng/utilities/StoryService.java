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

import java.util.List;

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
            String maxId = "SELECT MAX(ID) FROM STORIE";
            int currentStoryId = jdbcTemplate.queryForObject(maxId, Integer.class);
            httpSession.setAttribute("currentStoryId", currentStoryId);

        } catch (DataAccessException e) {
            logger.error("Lanciata eccezione nel metodo createStory della classe DBHandler. Causa dell'eccezione: {}. Descrizione dell'eccezione: {}", e.getCause(), e.getMessage());
            throw e;
        }
    }
    public List<Story> getStories() {
        try {

            return jdbcTemplate.query("SELECT * FROM STORIE", new StoryRowMapper());


        } catch (DataAccessException e) {
            logger.error("Lanciata eccezione nel metodo getStories della classe DBHandler. Causa dell'eccezione: {}. Descrizione dell'eccezione: {}", e.getCause(), e.getMessage());
            return null;

        }
    }
}

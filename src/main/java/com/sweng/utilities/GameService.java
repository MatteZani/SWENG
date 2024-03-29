package com.sweng.utilities;

import com.sweng.entity.GameSession;
import com.sweng.entity.Scenario;
import com.sweng.entity.Story;
import com.sweng.entity.User;
import com.sweng.mapper.ScenarioRowMapper;
import com.sweng.mapper.StoryRowMapper;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataAccessException;

@Service
public class GameService {

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    Logger logger = LoggerFactory.getLogger(GameService.class);

    public GameSession startNewGame(int storyId) {
        try {
            String sql = "SELECT * FROM STORIE WHERE ID = ?";
            Story story = jdbcTemplate.queryForObject(sql, new StoryRowMapper(), storyId);
            User user = (User) httpSession.getAttribute("user"); // Casting da Object a User

            if (user == null) {
                throw new IllegalStateException("Nessun utente è attualmente loggato.");
            }

            GameSession gameSession = new GameSession(user, story);
            httpSession.setAttribute("gameSession", gameSession);
            return gameSession;
        } catch (DataAccessException e) {
            logger.error("Errore nel metodo startNewGame: {}", e.getMessage());
            throw e;
        }
    }


//    public Scenario chooseNextScenario(int scenarioId) {
//        try {
//            String sql = "SELECT * FROM SCENARI WHERE ID = ?";
//            Scenario scenario = jdbcTemplate.query(sql, scenarioId);
//            return scenario;
//        } catch (DataAccessException e) {
//            logger.error("Errore nel metodo chooseNextScenario: {}", e.getMessage());
//            throw e;
//        }
//    }
}

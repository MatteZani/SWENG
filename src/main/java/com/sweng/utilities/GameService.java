package com.sweng.utilities;

import com.sweng.entity.*;
import com.sweng.mapper.StoryRowMapper;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
public class GameService {

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private ScenarioService scenarioService;

    @Autowired
    private ElementService elementService;

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

    public String loadScenario(Scenario scenario, Model model){

        List<Scenario> nextScenarios = scenarioService.getNextScenariosByScenarioId(scenario.getId());
        scenario.setNextScenarios(nextScenarios);

        model.addAttribute("scenario", scenario);

        if(scenario.getFoundObjectId() != 0){
            StoryObject foundObject = elementService.getStoryObjectById(scenario.getFoundObjectId());
            model.addAttribute("foundObjectMessage", "Ti è stato aggiunto all'inventario il seguente oggetto: " + foundObject.getName());
        }

        if(scenario.getRiddleId() != 0){
            Riddle riddle = elementService.getRiddleById(scenario.getRiddleId());
            model.addAttribute("riddleMessage", "Per continuare devi rispondere al seguente indovinello: " + riddle.getQuestion());
            model.addAttribute("riddle", riddle);
        }

        return "play-story";
    }
}

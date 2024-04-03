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

        if(nextScenarios.size() == 0){
            model.addAttribute("endStoryMessage", "Sei arrivato alla fine della storia");
        }
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

    public void saveGameSession(GameSession gameSession) {
        String checkSql = "SELECT COUNT(*) FROM sessione WHERE username = ? AND story_id = ?";
        int count = jdbcTemplate.queryForObject(checkSql, new Object[]{gameSession.getUser().getUsername(), gameSession.getStoryId()}, Integer.class);

        if (count > 0) {
            // Aggiorna la sessione esistente
            String updateSql = "UPDATE sessione SET current_scenario_id = ? WHERE username = ? AND story_id = ?";
            jdbcTemplate.update(updateSql, gameSession.getCurrentScenario(), gameSession.getUser().getUsername(), gameSession.getStoryId());
        } else {
            // Crea una nuova sessione
            String insertSql = "INSERT INTO sessione (username, story_id, current_scenario_id) VALUES (?, ?, ?)";
            jdbcTemplate.update(insertSql, gameSession.getUser().getUsername(), gameSession.getStoryId(), gameSession.getCurrentScenario());
        }
    }

    public GameSession loadGameSession(String username, int storyId) {
        try {
            String sql = "SELECT * FROM sessione WHERE username = ? AND story_id = ?";
            return jdbcTemplate.queryForObject(sql, new Object[]{username, storyId}, (rs, rowNum) -> {
                User user = new User(); // Dovresti avere un modo per caricare l'utente basato su username
                user.setUsername(rs.getString("username"));

                Story story = new Story(); // Dovresti avere un modo per caricare la storia basato su storyId
                story.setId(rs.getInt("story_id"));

                GameSession gameSession = new GameSession(user, story);
                gameSession.setCurrentScenario(rs.getInt("current_scenario_id"));
                // logica inventario??

                return gameSession;
            });
        } catch (Exception e) {
            return null;
        }
    }



}

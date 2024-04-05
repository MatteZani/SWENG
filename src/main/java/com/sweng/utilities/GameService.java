package com.sweng.utilities;

import com.sweng.entity.GameSession;
import com.sweng.entity.Riddle;
import com.sweng.entity.Scenario;
import com.sweng.entity.StoryObject;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    private StoryService storyService;
    @Autowired
    private UserService userService;

    @Autowired
    private ElementService elementService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    Logger logger = LoggerFactory.getLogger(GameService.class);

    public String loadScenario(Scenario scenario, Model model){

        List<Scenario> nextScenarios = scenarioService.getNextScenariosByScenarioId(scenario.getId());

        if(nextScenarios.size() == 0){
            model.addAttribute("endStoryMessage", "Sei arrivato alla fine della storia");
        }
        scenario.setNextScenarios(nextScenarios);

        model.addAttribute("scenario", scenario);


        int storyId = storyService.getStoryIdByScenarioId(scenario.getId());
        // Aggiungo l'ID della storia al model
        model.addAttribute("storyId", storyId);

        if(scenario.getFoundObjectId() != 0){
            StoryObject foundObject = elementService.getStoryObjectById(scenario.getFoundObjectId());
            String username = (String) httpSession.getAttribute("username");
            elementService.addObjectToInventory(storyId, scenario.getFoundObjectId(), username);
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
        String checkSql = "SELECT COUNT(*) FROM PARTITE WHERE USERNAME = ? AND ID_STORIA = ?";
        // Usare gli argomenti direttamente nella chiamata
        int count = jdbcTemplate.queryForObject(checkSql, Integer.class, gameSession.getUser().getUsername(), gameSession.getCurrentStory().getId());

        if (count > 0) {
            // Aggiorna la sessione esistente
            String updateSql = "UPDATE PARTITE SET SCENARIO_CORRENTE = ? WHERE USERNAME = ? AND ID_STORIA = ?";
            jdbcTemplate.update(updateSql, gameSession.getCurrentScenario(), gameSession.getUser().getUsername(), gameSession.getCurrentStory().getId());
        } else {
            // Crea una nuova sessione
            String insertSql = "INSERT INTO PARTITE (USERNAME, ID_STORIA, SCENARIO_CORRENTE) VALUES (?, ?, ?)";
            jdbcTemplate.update(insertSql, gameSession.getUser().getUsername(), gameSession.getCurrentStory().getId(), gameSession.getCurrentScenario());
        }
    }

    public Integer deleteGameSession(Integer storyId, String username){
        String sql = "DELETE FROM PARTITE WHERE USERNAME = ? AND ID_STORIA = ?";
        jdbcTemplate.update(sql, username, storyId);
        return storyId;
    }

}
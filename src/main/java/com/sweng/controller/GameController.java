package com.sweng.controller;

import com.sweng.entity.GameSession;
import com.sweng.entity.Riddle;
import com.sweng.entity.Scenario;
import com.sweng.entity.User;
import com.sweng.utilities.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GameController {

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private GameService gameService;

    @Autowired
    private ScenarioService scenarioService;

    @Autowired
    private UserService userService;

    @Autowired
    private ElementService elementService;
    @Autowired
    private StoryService storyService;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @PostMapping("/choose-scenario")
    public String chooseNextScenario(@RequestParam int scenarioId, Model model) {
        String username = (String) httpSession.getAttribute("username");
        if (username == null) {
            return "login";
        }

        Scenario scenario = scenarioService.getScenarioById(scenarioId);

        if (scenario.getNecessaryObjectId() != 0) {
            if (!elementService.checkObjectInInventory(username, scenario.getNecessaryObjectId())) {
                model.addAttribute("error", "Non possiedi l'oggetto necessario per accedere a questo scenario.");
                model.addAttribute("scenario", scenario);

                return gameService.loadScenario(scenario, model);
            }
        }

        if (scenario.getFoundObjectId() != 0) {
            elementService.addObjectToInventory(storyService.getStoryIdByScenarioId(scenarioId), scenario.getFoundObjectId(), username);
        }

        return gameService.loadScenario(scenario, model);
    }


    @PostMapping("/process-choice")
    public String processChoice(@RequestParam int scenarioId, @RequestParam String response, Model model) {
        String username = (String) httpSession.getAttribute("username");
        if (username == null) {
            return "login";
        }

        Scenario scenario = scenarioService.getScenarioById(scenarioId);
        Riddle riddle = elementService.getRiddleById(scenario.getRiddleId());

        Scenario scenarioGiusto = scenarioService.getScenarioGiusto(scenarioId);
        Scenario scenarioSbagliato = scenarioService.getScenarioSbagliato(scenarioId);

        if (response.equals(riddle.getAnswer())) {
            return chooseNextScenario(scenarioGiusto.getId(), model);
        } else {
            return chooseNextScenario(scenarioSbagliato.getId(), model);
        }
    }

    @PostMapping("/save-game")
    public String saveGame(@RequestParam("storyId") Integer storyId, Integer scenarioId, Model model) {
        String username = (String) httpSession.getAttribute("username");
        if (username == null) {
            return "login";
        }
        // Recupero l'utente nella httpSession
        User user = userService.getUserByUsername((String) httpSession.getAttribute("username"));

        GameSession gameSession = new GameSession(storyService.getStoryById(storyId), user, scenarioId); //(GameSession) httpSession.getAttribute("gameSession");
        gameService.saveGameSession(gameSession);

        model.addAttribute("stories", storyService.getStories());
        model.addAttribute("savedStories", storyService.getSavedIdStories(user.getUsername()));
        model.addAttribute("savedMessage", "Storia salvata con successo!");

        return "catalog";
    }

    @GetMapping("/load-game")
    public String loadGame(@RequestParam("storyId") int storyId, Model model) {
        String username = (String) httpSession.getAttribute("username");
        if (username == null) {
            return "login";
        }
        User user = userService.getUserByUsername((String) httpSession.getAttribute("username"));

        String sql = "SELECT SCENARIO_CORRENTE FROM PARTITE WHERE USERNAME = ? AND ID_STORIA = ?";
        Integer currentScenarioId = jdbcTemplate.queryForObject(sql, Integer.class, user.getUsername(), storyId);
        Scenario currentScenario = scenarioService.getScenarioById(currentScenarioId);

        return gameService.loadScenario(currentScenario, model);
    }

    @GetMapping("/cancel-game")
    public String cancelGame(@RequestParam("storyId") int storyId, Model model){
        String username = (String) httpSession.getAttribute("username");
        if (username == null) {
            return "login";
        }

        gameService.deleteGameSession(storyId, username);
        elementService.deleteObjectFromInventory(storyId, username);

        model.addAttribute("stories", storyService.getStories());
        model.addAttribute("savedStories", storyService.getSavedIdStories(username));

        return "catalog";
    }

}
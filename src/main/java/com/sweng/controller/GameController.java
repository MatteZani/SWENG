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

    @PostMapping("/pagina-prova")
    public String prova(@RequestParam String response, Model model) {
        model.addAttribute("Risposta", response);

        return "prova";
    }

    @PostMapping("/choose-scenario")
    public String chooseNextScenario(@RequestParam int scenarioId, Model model) {
        String username = (String) httpSession.getAttribute("username");

        if (username == null) {
            return "redirect:/login";
        }
        //GameSession gameSession = (GameSession) httpSession.getAttribute("gameSession");

        Scenario scenario = scenarioService.getScenarioById(scenarioId);
        //User user = userService.getUserByUsername((String) httpSession.getAttribute("username"));
        //int storyId = storyService.getStoryIdByScenarioId(scenarioId);

        if (scenario.getNecessaryObjectId() != 0) {
            if (!elementService.checkObjectInInventory(username, scenario.getNecessaryObjectId())) {
                model.addAttribute("error", "Non possiedi l'oggetto necessario per accedere a questo scenario.");

                //scenario = scenarioService.getScenarioById(scenarioId);
                model.addAttribute("scenario", scenario);

                return gameService.loadScenario(scenario, model);
            }
        }

        if (scenario.getFoundObjectId() != 0) {
            elementService.addObjectToInventory(storyService.getStoryIdByScenarioId(scenarioId), scenario.getFoundObjectId(), username);
            //gameSession.addToInventory(scenario.getFoundObjectId());
        }

        //gameSession.setCurrentScenario(scenarioId);
        //httpSession.setAttribute("gameSession", gameSession); // Aggiorna la sessione di gioco con il nuovo stato
        return gameService.loadScenario(scenario, model);
    }


    @PostMapping("/process-choice")
    public String processChoice(@RequestParam int scenarioId, @RequestParam String response, Model model) {

        String username = (String) httpSession.getAttribute("username");

        if (username == null) {
            return "redirect:/login";
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
        // Recupero l'utente nella httpSession
        User user = userService.getUserByUsername((String) httpSession.getAttribute("username"));

        GameSession gameSession = new GameSession(storyService.getStoryById(storyId), user, scenarioId); //(GameSession) httpSession.getAttribute("gameSession");
        gameService.saveGameSession(gameSession);

        model.addAttribute("stories", storyService.getStories());
        model.addAttribute("savedStories", storyService.getSavedIdStories(user.getUsername()));
        model.addAttribute("savedMessage", "Storia salvata con successo!");

        return "catalog";
        //return "homepage";
    }

       @GetMapping("/load-game")
    public String loadGame(@RequestParam("storyId") int storyId, Model model) {
        User user = userService.getUserByUsername((String) httpSession.getAttribute("username"));
        if (user == null) {
            return "redirect:/login"; // Usa redirect per evitare problemi con il refresh della pagina
        }

        String sql = "SELECT SCENARIO_CORRENTE FROM PARTITE WHERE USERNAME = ? AND ID_STORIA = ?";
        Integer currentScenarioId = jdbcTemplate.queryForObject(sql, Integer.class, user.getUsername(), storyId);
        Scenario currentScenario = scenarioService.getScenarioById(currentScenarioId);

        return gameService.loadScenario(currentScenario,model);

        /*GameSession gameSession = gameService.loadGameSession(user.getUsername(), storyId);
        if (gameSession == null) {
            model.addAttribute("error", "Impossibile trovare una sessione di gioco salvata.");
            return "catalog"; // Assicurati che "catalog" sia il template giusto per mostrare il messaggio di errore
        }
        httpSession.setAttribute("gameSession", gameSession);
        return "redirect:/play-story"; // Usa redirect per caricare lo scenario corrente della storia*/
    }

}
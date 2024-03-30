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


    @GetMapping("/start/{storyId}")
    public String startGame(int storyId, Model model, HttpSession session) {
        String username = (String) httpSession.getAttribute("username");
        if(username==null){
            return "login";
        }

        GameSession gameSession = gameService.startNewGame(storyId);
        session.setAttribute("gameSession", gameSession); // Imposta la gameSession nella HttpSession

        model.addAttribute("scenario", gameSession.getCurrentScenario()); // Aggiungi lo scenario corrente al modello

        return "play-story"; // Nome del template Thymeleaf per iniziare la storia
    }


    @PostMapping("/pagina-prova")
    public String prova(@RequestParam String response, Model model){
        model.addAttribute("Risposta", response);

        return "prova";
    }

    @PostMapping("/choose-scenario")
    public String chooseNextScenario(@RequestParam int scenarioId, Model model) {
        //logger.info("Choosing next scenario with ID: {}", scenarioId);
        Scenario scenario = scenarioService.getScenarioById(scenarioId);
        User user = userService.getUserByUsername((String) httpSession.getAttribute("username"));
        int storyId = storyService.getStoryIdByScenarioId(scenarioId);
        GameSession gameSession = new GameSession(user, storyService.getStoryById(storyId));
        //GameSession gameSession = (GameSession) httpSession.getAttribute("gameSession");
        //int storyId = gameSession.getCurrentStory().getId(); // Ottieni l'ID della storia dalla sessione di gioco


       /*user = (User) httpSession.getAttribute("user");
        if (user == null) {
            // Gestisci l'assenza dell'utente, forse reindirizzando al login o visualizzando un errore
            return "redirect:/login";
        }

        gameSession = (GameSession) httpSession.getAttribute("gameSession");
        if (gameSession == null) {
            // Gestisci l'assenza della sessione di gioco
            return "redirect:/start"; // Usa il percorso appropriato per iniziare una nuova sessione di gioco
        }*/


        if (scenario.getNecessaryObjectId() != 0) {
            if (!elementService.checkObjectInInventory(user.getUsername(), scenario.getNecessaryObjectId())) {
                model.addAttribute("error", "Non possiedi l'oggetto necessario per accedere a questo scenario.");

                scenario = scenarioService.getScenarioById(gameSession.getCurrentScenario());
                model.addAttribute("scenario", scenario);

                return gameService.loadScenario(scenario, model);
            }
        }

        if (scenario.getFoundObjectId() != 0) {
            elementService.addObjectToInventory(storyId, scenario.getFoundObjectId(), user.getUsername());
            gameSession.addToInventory(scenario.getFoundObjectId());
        }

        gameSession.setCurrentScenario(scenarioId);
        return gameService.loadScenario(scenario, model);
    }




    @PostMapping("/process-choice")
    public String processChoice(@RequestParam int scenarioId, @RequestParam String response, Model model){
        Scenario scenario = scenarioService.getScenarioById(scenarioId);
        Riddle riddle = elementService.getRiddleById(scenario.getRiddleId());

        Scenario scenarioGiusto = scenarioService.getScenarioGiusto(scenarioId);
        Scenario scenarioSbagliato = scenarioService.getScenarioSbagliato(scenarioId);

        if(response.equals(riddle.getAnswer())){
            return chooseNextScenario(scenarioGiusto.getId(), model);
        }
        else{
            return chooseNextScenario(scenarioSbagliato.getId(), model);
        }
    }


}

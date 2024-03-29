package com.sweng.controller;

import com.sweng.entity.GameSession;
import com.sweng.entity.Riddle;
import com.sweng.entity.Scenario;
import com.sweng.entity.StoryObject;
import com.sweng.utilities.ElementService;
import com.sweng.utilities.GameService;
import com.sweng.utilities.ScenarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class GameController {

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private GameService gameService;

    @Autowired
    private ScenarioService scenarioService;

    @Autowired
    private ElementService elementService;

    @Autowired
    JdbcTemplate jdbcTemplate;


    @GetMapping("/start/{storyId}")
    public String startGame(int storyId, Model model) {
        GameSession gameSession = gameService.startNewGame(storyId);
        model.addAttribute("scenario", gameSession.getCurrentScenario());

        return "play-story";
    }

    @PostMapping("/choose-scenario")
    public String chooseNextScenario(@RequestParam int scenarioId, @RequestParam(required = false) String outcome, Model model) {

        List<Scenario> nextScenarios = null;
        Scenario scenario = scenarioService.getScenarioById(scenarioId);
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
        if(outcome != null && outcome.equals("true")){
            nextScenarios = scenarioService.getNextScenariosByScenarioId(scenarioId);
        }
        else if(outcome != null && outcome.equals("false")){

            String descrizione = "Risposta sbagliata";

            String sql = "SELECT SCENARIO_ARRIVO FROM COLLEGAMENTI WHERE SCENARIO_PARTENZA = ? AND DESCRIZIONE = ?";
            ArrayList<Integer> nextScenariosId = (ArrayList<Integer>) jdbcTemplate.queryForList(sql, Integer.class, scenarioId, descrizione);

            nextScenarios.add(scenarioService.getScenarioById(nextScenariosId.get(0)));

        }
        model.addAttribute("nextScenarios", nextScenarios);

        return "play-story";
    }

//    @GetMapping("/process-choice")
//    public String processChoice(@RequestParam("response") String response, @RequestParam("riddleAnswer") String riddleAnswer, @RequestParam("scenarioId") Integer scenarioId, Model model){
//
////        Integer nextScenarioId = elementService.processChoice(scenarioId, response.equals(riddleAnswer));
////
////        Scenario nextScenario = scenarioService.getScenarioById(nextScenarioId);
//
//        ArrayList<Scenario> nextScenarios = elementService.processChoice(scenarioId, response.equals(riddleAnswer));
//        model.addAttribute("nextScenarios", nextScenarios);
//
//        if(nextScenario.getFoundObjectId() != 0){
//            StoryObject foundObject = elementService.getStoryObjectById(nextScenario.getFoundObjectId());
//            model.addAttribute("foundObjectMessage", "Ti è stato aggiunto all'inventario il seguente oggetto: " + foundObject.getName());
//        }
//
//        if(nextScenario.getRiddleId() != 0){
//            Riddle riddle = elementService.getRiddleById(nextScenario.getRiddleId());
//            model.addAttribute("riddleMessage", "Per continuare devi rispondere al seguente indovinello: " + riddle.getQuestion());
//            model.addAttribute("riddle", riddle);
//        }
//        return "play-story";
//    }


//    @GetMapping("/play-scenario/{scenarioId}")
//    public String playScenario(int scenarioId, Model model) {
//        Scenario scenario = scenarioService.getScenarioById(scenarioId);
//        List<Scenario> nextScenarios = scenarioService.getNextScenariosByScenarioId(scenarioId);
//
//        model.addAttribute("scenario", scenario);
//        // Aggiunta e controllo dei prossimi scenari con Thymeleaf
//        model.addAttribute("nextScenarios", nextScenarios);
//
//
//
//        return "play-story";
//    }

}

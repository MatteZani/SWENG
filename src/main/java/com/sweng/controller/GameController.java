package com.sweng.controller;

import com.sweng.entity.GameSession;
import com.sweng.entity.Riddle;
import com.sweng.entity.Scenario;
import com.sweng.utilities.ElementService;
import com.sweng.utilities.GameService;
import com.sweng.utilities.ScenarioService;
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
    private ElementService elementService;

    @Autowired
    JdbcTemplate jdbcTemplate;


    @GetMapping("/start/{storyId}")
    public String startGame(int storyId, Model model) {
        GameSession gameSession = gameService.startNewGame(storyId);
        model.addAttribute("scenario", gameSession.getCurrentScenario());

        return "play-story";
    }

    @PostMapping("/pagina-prova")
    public String prova(@RequestParam String response, Model model){
        model.addAttribute("Risposta", response);

        return "prova";
    }

    @PostMapping("/choose-scenario")
    public String chooseNextScenario(@RequestParam int scenarioId, Model model) {

        //TODO aggiungere il controllo se l'utente possiede l'oggetto necessario per entrare nello scenario

        return gameService.loadScenario(scenarioService.getScenarioById(scenarioId), model);

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

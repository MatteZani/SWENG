package com.sweng.controller;

import com.sweng.entity.GameSession;
import com.sweng.entity.Scenario;
import com.sweng.utilities.ElementService;
import com.sweng.utilities.GameService;
import com.sweng.utilities.ScenarioService;
import com.sweng.utilities.StoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/game")
public class GameController {

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private GameService gameService;

    @Autowired
    private ScenarioService scenarioService;

    @Autowired
    JdbcTemplate jdbcTemplate;


    @GetMapping("/start/{storyId}")
    public String startGame(int storyId, Model model) {
        GameSession gameSession = gameService.startNewGame(storyId);
        model.addAttribute("scenario", gameSession.getCurrentScenario());

        return "play-story";
    }

    @PostMapping("/chooseScenario")
    public String chooseScenario(@RequestParam String nextScenarioId, Model model) {
        int id = Integer.parseInt(nextScenarioId);
        Scenario nextScenario = gameService.chooseNextScenario(id);
        model.addAttribute("scenario", nextScenario);

        return "play-story";
    }


    @GetMapping("/play-scenario/{scenarioId}")
    public String playScenario(@PathVariable int scenarioId, Model model) {
        Scenario scenario = scenarioService.getScenarioById(scenarioId); // Supponendo che questo metodo esista e recuperi lo scenario corrente
        List<Scenario> nextScenarios = scenarioService.getNextScenariosByScenarioId(scenarioId); // Metodo ipotetico per ottenere i prossimi scenari

        model.addAttribute("scenario", scenario);
        model.addAttribute("nextScenarios", nextScenarios); // Aggiungi i prossimi scenari al modello per Thymeleaf

        return "play-story";
    }

}

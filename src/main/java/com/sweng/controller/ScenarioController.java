package com.sweng.controller;

import com.sweng.entity.Scenario;
import com.sweng.utilities.ScenarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

@Controller
public class ScenarioController {

    @Autowired
    private HttpSession httpSession;

    // URL di connessione al database
    @Autowired
    private ScenarioService scenarioService;


    @Autowired
    JdbcTemplate jdbcTemplate;


    @GetMapping("/create-scenario")
    public String createInitialScenario(Model model){
        model.addAttribute("message", "Crea lo scenario iniziale della storia");

        model.addAttribute("necessaryObjectMessage", "Vuoi rendere un oggetto necessario per entrare in questo scenario?");
        model.addAttribute("gainObjectMessage", "Vuoi aggiungere un oggetto all'inventario dei giocatori che entrano in questo scenario?");
        model.addAttribute("riddleMessage", "Vuoi associare un indovinello a questo scenario? La scelta del giocatore condizionerà il suo percorso");
        model.addAttribute("currentStoryObjects", httpSession.getAttribute("currentStoryObjects") );
        model.addAttribute("currentRiddles", httpSession.getAttribute("currentRiddles"));
        return "create-initial-scenario";
    }


    @PostMapping("/create-scenario/process")
    public String createScenario(@RequestParam String scenarioDescription, @RequestParam(required = false) Integer necessaryObjectId, @RequestParam(required = false) Integer foundObjectId, @RequestParam(required = false) Integer riddleId, Model model){
        int storyId = (Integer) httpSession.getAttribute("currentStoryId");

        // se non viene selezionato alcun oggetto
        int finalNecessaryObjectId = (necessaryObjectId != null) ? necessaryObjectId : 0;
        int finalFoundObjectId = (foundObjectId != null) ? foundObjectId : 0;

        Scenario scenario = scenarioService.createScenario(storyId, scenarioDescription, finalNecessaryObjectId, finalFoundObjectId, riddleId);

        // Aggiorna la lista degli scenari in sessione
        ArrayList<Scenario> scenarios = (ArrayList<Scenario>) httpSession.getAttribute("scenarios");
        if (scenarios == null) {
            scenarios = new ArrayList<>();
            scenarioService.setInitialScenario(storyId, scenarioService.getMaxScenarioId());
        }
        scenarios.add(scenario);
        httpSession.setAttribute("scenarios", scenarios);

        // Prepara il modello per la vista
        model.addAttribute("necessaryObjectMessage", "Vuoi rendere un oggetto necessario per entrare in questo scenario?");
        model.addAttribute("gainObjectMessage", "Vuoi aggiungere un oggetto all'inventario dei giocatori che entrano in questo scenario?");
        model.addAttribute("riddleMessage", "Vuoi associare un indovinello a questo scenario? La scelta del giocatore condizionerà il suo percorso");
        model.addAttribute("currentStoryObjects", httpSession.getAttribute("currentStoryObjects"));
        model.addAttribute("currentRiddles", httpSession.getAttribute("currentRiddles"));
        return "add-scenario";
    }


    @PostMapping("connect-scenarios")
    public String connectScenarios(Model model){
        model.addAttribute("message", "Connetti gli scenari che hai creato");

        model.addAttribute("scenarios", httpSession.getAttribute("scenarios"));

        System.out.println(httpSession.getAttribute("scenarios"));

        return "connect-scenarios";

    }

    @PostMapping("connect-scenarios/process")
    public String processConnection(@RequestParam("start") int startingScenario, @RequestParam("end") int endingScenario, Model model){


        scenarioService.connectScenarios(startingScenario, endingScenario,(Integer) httpSession.getAttribute("currentStoryId"));

        model.addAttribute("scenarios", httpSession.getAttribute("scenarios"));

        return "connect-scenarios";
    }

}

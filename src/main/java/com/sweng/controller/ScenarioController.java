package com.sweng.controller;

import com.sweng.entity.Scenario;
import com.sweng.entity.ScenarioBuilder;
import com.sweng.entity.User;
import com.sweng.utilities.ScenarioService;
import com.sweng.utilities.UserService;
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
    private ScenarioService scenarioservice;

    @Autowired
    JdbcTemplate jdbcTemplate;


    @GetMapping("/create-scenario")
    public String createInitialScenario(Model model){
        model.addAttribute("message", "Crea lo scenario iniziale della storia");

        model.addAttribute("necessaryObjectMessage", "Vuoi rendere un oggetto necessario per entrare in questo scenario?");
        model.addAttribute("gainObjectMessage", "Vuoi aggiungere un oggetto all'inventario dei giocatori che entrano in questo scenario?");
        model.addAttribute("currentStoryObjects", httpSession.getAttribute("currentStoryObjects") );
        model.addAttribute("currentRiddles", httpSession.getAttribute("currentRiddles") );
        return "create-initial-scenario";
    }


    @PostMapping("/create-scenario/process")
    public String createScenario(@RequestParam String scenarioDescription, @RequestParam int necessaryObjectId , @RequestParam int foundObjectId, Model model){
        //TODO cambiare valore 0 passato nella creazione dello scenario, relativo all'oggetto della storia

        scenarioservice.createScenario((Integer) httpSession.getAttribute("currentStoryId"), scenarioDescription, necessaryObjectId, foundObjectId);
        scenarioservice.addScenarioToStory((Integer) httpSession.getAttribute("currentStoryId"));

        ArrayList<Scenario> scenarios;
        if(httpSession.getAttribute("scenarios") == null){
            scenarios = new ArrayList<Scenario>();
        }
        else{
            scenarios = (ArrayList<Scenario>) httpSession.getAttribute("scenarios");
        }

        scenarios.add(new ScenarioBuilder().setId(scenarioservice.getMaxScenarioId()).setDescription(scenarioDescription).setStoryId((Integer) httpSession.getAttribute("currentStoryId")).setNecessaryObjectId(0).build());
        httpSession.setAttribute("scenarios", scenarios);

        model.addAttribute("necessaryObjectMessage", "Vuoi rendere un oggetto necessario per entrare in questo scenario?");
        model.addAttribute("gainObjectMessage", "Vuoi aggiungere un oggetto all'inventario dei giocatori che entrano in questo scenario?");
        model.addAttribute("currentStoryObjects", httpSession.getAttribute("currentStoryObjects") );
        model.addAttribute("currentRiddles", httpSession.getAttribute("currentRiddles") );
        return "add-scenario";
    }


}

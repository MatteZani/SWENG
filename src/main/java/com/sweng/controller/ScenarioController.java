package com.sweng.controller;

import com.sweng.entity.Scenario;
import com.sweng.entity.Story;
import com.sweng.utilities.ScenarioService;
import com.sweng.utilities.StoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ScenarioController {

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private ScenarioService scenarioService;

    @Autowired
    private StoryService storyService;

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
        String username = (String) httpSession.getAttribute("username");
        if(username != null){
            model.addAttribute("username", username);
            return "homepage";
        }

        int storyId = (Integer) httpSession.getAttribute("currentStoryId");

        // se non viene selezionato alcun oggetto
        int finalNecessaryObjectId = (necessaryObjectId != null) ? necessaryObjectId : 0;
        int finalFoundObjectId = (foundObjectId != null) ? foundObjectId : 0;

        // se non viene selezionato alcun indovinello
        int finalRiddleId = (riddleId != null) ? riddleId : 0;

        Scenario scenario = scenarioService.createScenario(storyId, scenarioDescription, finalNecessaryObjectId, finalFoundObjectId, finalRiddleId);

        ArrayList<Scenario> scenarios = (ArrayList<Scenario>) httpSession.getAttribute("scenarios");
        if (scenarios == null) {
            scenarios = new ArrayList<>();
            scenarioService.setInitialScenario(storyId, scenarioService.getMaxScenarioId());
        }
        scenarios.add(scenario);
        httpSession.setAttribute("scenarios", scenarios);

        model.addAttribute("necessaryObjectMessage", "Vuoi rendere un oggetto necessario per entrare in questo scenario?");
        model.addAttribute("gainObjectMessage", "Vuoi aggiungere un oggetto all'inventario dei giocatori che entrano in questo scenario?");
        model.addAttribute("riddleMessage", "Vuoi associare un indovinello a questo scenario? La scelta del giocatore condizionerà il suo percorso");
        model.addAttribute("currentStoryObjects", httpSession.getAttribute("currentStoryObjects"));
        model.addAttribute("currentRiddles", httpSession.getAttribute("currentRiddles"));
        return "add-scenario";
    }


    @PostMapping("connect-scenarios")
    public String connectScenarios(Model model){
        String username = (String) httpSession.getAttribute("username");
        if(username != null){
            model.addAttribute("username", username);
            return "homepage";
        }

        model.addAttribute("message", "Connetti gli scenari che hai creato");
        model.addAttribute("scenarios", httpSession.getAttribute("scenarios"));

        return "connect-scenarios";
    }

    @PostMapping("connect-scenarios/process")
    public String processConnection(@RequestParam("start") int startingScenario,
                                    @RequestParam(value = "correctAnswer", required = false, defaultValue = "0") Integer correctAnswerScenario,
                                    @RequestParam(value = "wrongAnswer", required = false, defaultValue = "0") Integer wrongAnswerScenario,
                                    @RequestParam(value = "end", required = false, defaultValue = "0") Integer endingScenario,
                                    Model model) {

        String username = (String) httpSession.getAttribute("username");
        if(username != null){
            model.addAttribute("username", username);
            return "homepage";
        }

        Integer currentStoryId = (Integer) httpSession.getAttribute("currentStoryId");

        if (correctAnswerScenario != 0 && wrongAnswerScenario != 0) {
            // Logica per gli scenari con indovinelli
            scenarioService.connectScenarios(startingScenario, correctAnswerScenario, currentStoryId, "Risposta giusta");
            scenarioService.connectScenarios(startingScenario, wrongAnswerScenario, currentStoryId, "Risposta sbagliata");
        } else{
            // Logica per il collegamento normale
            Scenario nextScenario = scenarioService.getScenarioById(endingScenario);
            if(nextScenario.getNecessaryObjectId() != 0){

                scenarioService.connectScenarios(startingScenario, endingScenario, currentStoryId, "Oggetto necessario");
            }
            else{
                scenarioService.connectScenarios(startingScenario, endingScenario, currentStoryId, "Collegamento normale");
            }
        }

        model.addAttribute("scenarios", httpSession.getAttribute("scenarios"));
        return "connect-scenarios";
    }

    @PostMapping("/update-scenario")
    public String updateScenario(@RequestParam("scenarioId") Integer scenarioId,
                                 @RequestParam("newDescription") String newDescription,Model model) throws SQLException {

        String username = (String) httpSession.getAttribute("username");
        if(username != null){
            model.addAttribute("username", username);
            return "homepage";
        }

        scenarioService.updateDescription(scenarioId, newDescription);

        model.addAttribute("successMessage", "Scenario modificato con successo!");

        List<Story> stories = storyService.getStoriesByCreator((String) httpSession.getAttribute("username"));
        model.addAttribute("stories", stories);

        return "owner-catalog";
    }


}
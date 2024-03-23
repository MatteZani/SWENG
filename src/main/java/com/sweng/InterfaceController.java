package com.sweng;


import com.sweng.entity.Scenario;
import com.sweng.entity.Story;
import com.sweng.entity.User;
import com.sweng.utilities.DBHandler;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.*;

@Controller
public class InterfaceController {

    @Autowired
    private HttpSession httpSession;

    // URL di connessione al database
    @Autowired
    private DBHandler dbHandler;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @GetMapping("/menu")
    public String menu(Model model){
        return "menu";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login/process")
    public String processLogin(@RequestParam("username") String username, @RequestParam("password") String password,
                               Model model) {
        boolean isValidCredentials = dbHandler.verifyCredentials(username, password);
        if (isValidCredentials) {
            httpSession.setAttribute("username", username);
            httpSession.setAttribute("password", password);
            return "homepage";
        } else {
            // Reindirizza alla pagina di login con un messaggio di errore se le credenziali non sono valide
            model.addAttribute("error", "Credenziali non valide");
            return "login-failed"; // Assicurati che "login" sia il nome corretto della tua pagina di login
        }
    }

    @GetMapping("/registration")
    public String registration(Model model){
        return "registration";
    }


    @PostMapping("/registration/process")
    public String processRegistration(@RequestParam("username") String username, @RequestParam("password") String password, Model model) {
        User user = new User(username, password);
        dbHandler.saveUser(user);

        httpSession.setAttribute("username", username);
        httpSession.setAttribute("password", password);

        // Reindirizzamento alla pagina "avvenuta registrazione"
        return "homepage";
    }

    @GetMapping("/catalog")
    public String catalog() {
        return "catalog";
    }

    @GetMapping("/create-story")
    public String createStory(Model model){
        return "create-story";
    }

    @PostMapping("/create-story/process")
    public String processCreateStory(@RequestParam("title") String title, @RequestParam("plot") String plot,
            @RequestParam("category") String category, Model model) {

//        // Creazione dello scenario iniziale
//        Scenario initial = new Scenario("Inizio", initialScenario);

        // Creazione della storia con lo scenario iniziale
        Story story = new Story(title, plot, null, (String) httpSession.getAttribute("username"), category);
        dbHandler.createStory(story);
        model.addAttribute("message", "Storia creata con successo, aggiungi lo scenario iniziale");

        // Chiamata al metodo createStory di DBHandler per salvare la storia nel database
        return "create-initial-scenario";
    }


    @PostMapping("/create-initial-scenario/process")
    public String createInitialScenario(@RequestParam String initialScenarioDescription, Model model){

        dbHandler.createScenario((Integer) httpSession.getAttribute("currentStoryId"), initialScenarioDescription);
        dbHandler.addScenarioToStory((Integer) httpSession.getAttribute("currentStoryId"));

        return "add-scenario";
    }

    @PostMapping("add-scenario/process")
    public String addScenarioToStory(@RequestParam String scenarioDescription, Model model){
        dbHandler.createScenario((Integer) httpSession.getAttribute("currentStoryId"), scenarioDescription);

        return "add-scenario";
    }

    @PostMapping("connect-scenarios")
    public String connectScenarios(Model model){
        model.addAttribute("message", "Connetti gli scenari che hai creato");

        return "connect-scenarios";

    }

    @PostMapping("connect-scenarios/process")
    public String processConnection(Model model){
        //TODO implementare metodo per fare inserire gli scenari da collegare all'utente
        model.addAllAttributes(dbHandler.getScenariosByStoryId((Integer) httpSession.getAttribute("currentStoryId")));

        dbHandler.connectScenarios(6, 7, 8);

        return "connect-scenarios";
    }

    @GetMapping("/play")
    public String play(Model model){
        return "play";
    }


}
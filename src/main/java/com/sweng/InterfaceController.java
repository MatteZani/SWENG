package com.sweng;


import com.sweng.entity.Scenario;
import com.sweng.entity.Story;
import com.sweng.entity.User;
import com.sweng.utilities.DBHandler;
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
            // Reindirizza alla pagina "catalogo" se le credenziali sono valide
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

        // Reindirizzamento alla pagina "avvenuta registrazione"
        return "homepage";
    }

    @GetMapping("/catalog")
    public String catalog() {
        return "catalog";
    }

    @GetMapping("/createStory")
    public String createStory(){
        return "create-story";
    }

    @PostMapping("/create-story/process")
    public String processCreateStory(
            @RequestParam("title") String title,
            @RequestParam("plot") String plot,
            @RequestParam("category") String category,
            @RequestParam("creatorId") int creatorId,
            @RequestParam("initialScenario") String initialScenario) {

        // Creazione dello scenario iniziale
        Scenario initial = new Scenario("Inizio", initialScenario);

        // Creazione della storia con lo scenario iniziale
        Story story = new Story(title, plot, initial, creatorId, category);
        dbHandler.createStory(story).getBody().toString();

        // Chiamata al metodo createStory di DBHandler per salvare la storia nel database
        return "catalog";
    }
    @GetMapping("/play")
    public String play(){
        return "play";
    }


}
package com.sweng;


import com.sweng.entity.*;
import com.sweng.utilities.DBHandler;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

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
    public String catalog(Model model) {
        model.addAttribute("stories", dbHandler.getStories());
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
        Story story = new StoryBuilder().setTitle(title).setPlot(plot).setInitialScenario(0).setCreator((String) httpSession.getAttribute("username")).setCategory(category).build();
        dbHandler.createStory(story);
        model.addAttribute("message", "Storia creata con successo, crea un oggetto che potrà essere utilizzato all'interno della storia");
        httpSession.setAttribute("currentStoryObjects", new ArrayList<StoryObject>());

        //model.addAttribute("objectCreationMessage", "Vuoi creare un oggetto relativo a questo scenario?" + "\n" + "Questo oggetto sarà necessario per entrare in questo scenario");
        // Chiamata al metodo createStory di DBHandler per salvare la storia nel database
        return "create-object";

    }

    @PostMapping("create-object/process")
    public String processCreateObject(@RequestParam("title") String title, @RequestParam("description") String description, Model model){
        StoryObject storyObject = new StoryObject(title, description);
        dbHandler.createObject(storyObject);
        storyObject.setId(dbHandler.getMaxObjectId());

        ArrayList<StoryObject> objects = (ArrayList<StoryObject>) httpSession.getAttribute("currentStoryObjects");
        objects.add(storyObject);
        httpSession.setAttribute("currentStoryObjects", objects);

        model.addAttribute("message", "Storia creata con successo, crea un oggetto che potrà essere utilizzato all'interno della storia");

        return "create-object";
    }

    @GetMapping("/create-riddle")
    public String showCreateRiddleForm(Model model) {

        return "create-riddle";
    }


    @PostMapping("create-riddle/process")
    public String processCreateRiddle(@RequestParam("quest") String quest, @RequestParam("rightAnswer") String rightAnswer, Model model){
        Riddle riddle = new Riddle(quest, rightAnswer);
        dbHandler.createRiddle(riddle);
        //riddle.setId(dbHandler.getMaxObjectId());
        ArrayList<Riddle> objects = (ArrayList<Riddle>) httpSession.getAttribute("currentStoryObjects");
        objects.add(riddle);
        httpSession.setAttribute("currentStoryObjects", objects);

        model.addAttribute("message", "Storia creata con successo, crea un indovinello che potrà essere risolto all'interno della storia");

        return "create-riddle";
    }

    @GetMapping("prova")
    public String prova(Model model){
        return "prova";
    }

    @GetMapping("/create-scenario")
    public String createInitialScenario(Model model){
        model.addAttribute("message", "Crea lo scenario iniziale della storia");

        model.addAttribute("necessaryObjectMessage", "Vuoi rendere un oggetto necessario per entrare in questo scenario?");
        model.addAttribute("gainObjectMessage", "Vuoi aggiungere un oggetto all'inventario dei giocatori che entrano in questo scenario?");
        model.addAttribute("currentStoryObjects", httpSession.getAttribute("currentStoryObjects") );
        return "create-initial-scenario";
    }


    @PostMapping("/create-scenario/process")
    public String createScenario(@RequestParam String scenarioDescription, @RequestParam int necessaryObjectId , @RequestParam int foundObjectId, Model model){
        //TODO cambiare valore 0 passato nella creazione dello scenario, relativo all'oggetto della storia

        dbHandler.createScenario((Integer) httpSession.getAttribute("currentStoryId"), scenarioDescription, necessaryObjectId, foundObjectId);
        dbHandler.addScenarioToStory((Integer) httpSession.getAttribute("currentStoryId"));

        ArrayList<Scenario> scenarios;
        if(httpSession.getAttribute("scenarios") == null){
            scenarios = new ArrayList<Scenario>();
        }
        else{
            scenarios = (ArrayList<Scenario>) httpSession.getAttribute("scenarios");
        }

        scenarios.add(new ScenarioBuilder().setId(dbHandler.getMaxScenarioId()).setDescription(scenarioDescription).setStoryId((Integer) httpSession.getAttribute("currentStoryId")).setNecessaryObjectId(0).build());
        httpSession.setAttribute("scenarios", scenarios);

        model.addAttribute("necessaryObjectMessage", "Vuoi rendere un oggetto necessario per entrare in questo scenario?");
        model.addAttribute("gainObjectMessage", "Vuoi aggiungere un oggetto all'inventario dei giocatori che entrano in questo scenario?");
        model.addAttribute("currentStoryObjects", httpSession.getAttribute("currentStoryObjects") );
        return "add-scenario";
    }

//    @PostMapping("add-scenario/process")
//    public String addScenarioToStory(@RequestParam String scenarioDescription, @RequestParam int necessaryObjectId, @RequestParam int foundObjectId, Model model){
//
//        //TODO cambiare valore 0 passato nella creazione dello scenario, relativo all'oggetto della storia
//        dbHandler.createScenario((Integer) httpSession.getAttribute("currentStoryId"), scenarioDescription, necessaryObjectId, foundObjectId);
//
//        ArrayList<Scenario> scenarios = (ArrayList<Scenario>) httpSession.getAttribute("scenarios");
//        Scenario scenario = new ScenarioBuilder().setId(dbHandler.getMaxScenarioId()).setDescription(scenarioDescription).setStoryId((Integer) httpSession.getAttribute("currentStoryId")).build();
//        scenarios.add(scenario);
//
//        httpSession.setAttribute("scenarios", scenarios);
//        return "add-scenario";
//    }

    @PostMapping("connect-scenarios")
    public String connectScenarios(Model model){
        model.addAttribute("message", "Connetti gli scenari che hai creato");

        model.addAttribute("scenarios", httpSession.getAttribute("scenarios"));

        System.out.println(httpSession.getAttribute("scenarios"));

        return "connect-scenarios";

    }

    @PostMapping("connect-scenarios/process")
    public String processConnection(@RequestParam("start") int startingScenario, @RequestParam("end") int endingScenario, Model model){


        dbHandler.connectScenarios(startingScenario, endingScenario,(Integer) httpSession.getAttribute("currentStoryId"));

        model.addAttribute("scenarios", httpSession.getAttribute("scenarios"));

        return "connect-scenarios";
    }

    @GetMapping("/play")
    public String play(Model model){
        return "play";
    }

    @GetMapping("/catalog/show-story")
    public String showStory(@RequestParam("storyId") Integer storyId, Model model){
        System.out.println(storyId);

        Story story = dbHandler.getStoryById(storyId);
        model.addAttribute("storyTitle", story.getTitle());
        model.addAttribute("storyPlot", story.getPlot());
        model.addAttribute("storyCategory", story.getCategory());
        model.addAttribute("scenariosNumber", dbHandler.getScenariosNumberByStoryId(storyId));
        return "story";
    }


}
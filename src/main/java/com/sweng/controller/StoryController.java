package com.sweng.controller;

import com.sweng.entity.Story;
import com.sweng.entity.StoryBuilder;
import com.sweng.entity.StoryObject;
import com.sweng.utilities.StoryService;
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
public class StoryController {
    @Autowired
    private HttpSession httpSession;

    // URL di connessione al database
    @Autowired
    private StoryService storyService;

    @Autowired
    JdbcTemplate jdbcTemplate;

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
        storyService.createStory(story);
        model.addAttribute("message", "Storia creata con successo, crea un oggetto che potrà essere utilizzato all'interno della storia");
        httpSession.setAttribute("currentStoryObjects", new ArrayList<StoryObject>());

        //model.addAttribute("objectCreationMessage", "Vuoi creare un oggetto relativo a questo scenario?" + "\n" + "Questo oggetto sarà necessario per entrare in questo scenario");
        // Chiamata al metodo createStory di DBHandler per salvare la storia nel database
        return "create-object";
        //return "create-story";

    }

    @GetMapping("/catalog")
    public String catalog(Model model) {
        model.addAttribute("stories", storyService.getStories());
        return "catalog";
    }
}

package com.sweng.controller;

import com.sweng.entity.Riddle;
import com.sweng.entity.StoryObject;
import com.sweng.utilities.ElementService;
import com.sweng.utilities.UserService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

@Controller
public class ElementController {


    Logger logger = LoggerFactory.getLogger(ElementController.class);
    @Autowired
    private HttpSession httpSession;

    // URL di connessione al database
    @Autowired
    private ElementService elementService;

    @Autowired
    JdbcTemplate jdbcTemplate;


    @PostMapping("create-object/process")
    public String processCreateObject(@RequestParam("title") String title, @RequestParam("description") String description, Model model){
        StoryObject storyObject = new StoryObject(title, description);
        elementService.createObject(storyObject);
        storyObject.setId(elementService.getMaxObjectId());

        ArrayList<StoryObject> objects = (ArrayList<StoryObject>) httpSession.getAttribute("currentStoryObjects");
        objects.add(storyObject);
        httpSession.setAttribute("currentStoryObjects", objects);

        model.addAttribute("message", "Storia creata con successo, crea un oggetto che potrà essere utilizzato all'interno della storia");

        return "create-object";
    }

    @GetMapping("/create-riddle")
    public String showCreateRiddleForm(Model model) {
        httpSession.setAttribute("currentRiddles", new ArrayList<Riddle>());

        return "create-riddle";
    }


    @PostMapping("create-riddle/process")
    public String processCreateRiddle(@RequestParam("quest") String quest, @RequestParam("rightAnswer") String rightAnswer, Model model){
        try {
            Riddle riddle = new Riddle(quest, rightAnswer);
            elementService.createRiddle(riddle);
            riddle.setId(elementService.getMaxRiddleId());
            //riddle.setId(dbHandler.getMaxObjectId());
            ArrayList<Riddle> riddles = (ArrayList<Riddle>) httpSession.getAttribute("currentRiddles");
            riddles.add(riddle);
            httpSession.setAttribute("currentRiddles", riddles);
        } catch (Exception e){
            logger.error("Errore nel metodo processCreateRiddle della classe ElementController. Causa errore: {}, tipo errore: {}, messaggio errore: {}", e.getCause(), e.getClass(), e.getMessage());
            model.addAttribute("message", "Si è verificato un errore durante la creazione dell'indovinello.");
            return "create-riddle";
        }

        model.addAttribute("message", "Storia creata con successo, crea un indovinello che potrà essere risolto all'interno della storia");

        return "create-riddle";
    }

}

package com.sweng.controller;

import com.sweng.entity.*;
import com.sweng.utilities.DBHandler;
import com.sweng.utilities.StoryService;
import com.sweng.utilities.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

    @org.springframework.web.bind.annotation.RestController
    @RequestMapping("/api/home")
    public class RestController {


        @Autowired
        private UserService userService;
        @Autowired
        private StoryService storyService;

        Logger logger = LoggerFactory.getLogger(RestController.class);

        @GetMapping("/user")
        public ResponseEntity<List<Map<String, Object>>> getUsers() {
            List<Map<String, Object>> users = userService.getUsers();
            return new ResponseEntity<>(users, HttpStatus.OK);
        }

        @GetMapping("/user/username")
        public ResponseEntity<Object> getUserByName(@RequestParam String username) {
            if (username.equals("pippo")) {
                User pippo = new User("pippo", "pippo");
                return new ResponseEntity<>(pippo, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Utente non trovato", HttpStatus.NOT_FOUND);
            }
        }

        /**
         * inserisce un nuovo utente nel database
         *
         * @param user l'utente da inserire
         * @return response entity con codice 200 e l'utente nel body se l'inserimento è andato a buon fide
         * response entity 400 con messaggio di errore se si verifica un errore nell'inserimento
         **/
        @PostMapping("/user")
        public ResponseEntity<Object> postUser(@RequestBody User user) {
            return userService.saveUser(user);
        }

        @GetMapping("/story")
        public ResponseEntity<Object> getStories(){
            List<Story> stories = storyService.getStories();

            if(stories == null)
                return new ResponseEntity<>("Errore nel recupero delle storie", HttpStatusCode.valueOf(400));
            else
                return new ResponseEntity<>(stories, HttpStatus.OK);
        }

        @PostMapping("/story")
        public ResponseEntity<Object> postStory(@RequestBody Story story){

            try {
                storyService.createStory(story);
                return new ResponseEntity<>(story, HttpStatus.OK);
            } catch (DataAccessException e) {
                return new ResponseEntity<>("Errore nel salvataggio della storia", HttpStatusCode.valueOf(400));
            }
        }

    }


   /*@PostMapping("/object")
    public ResponseEntity<Object> postObject(@RequestBody String name, @RequestBody String description){

        try {
            StoryObject storyObject = new StoryObject(name, description);
            dbHandler.createObject(storyObject);

            return new ResponseEntity<>(storyObject, HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>("Errore nella creazione dell'oggetto", HttpStatusCode.valueOf(400));
        }

    }

    @PostMapping("/scenario")
    public ResponseEntity<Object> postScenario(@RequestBody Scenario scenario){

        return dbHandler.createScenario(scenario);
    }

    @PostMapping("/riddle")
    public ResponseEntity<Object> postRiddle(@RequestBody Riddle riddle){

        return dbHandler.createRiddle(riddle);
    }

*/
/*
    @GetMapping("/scenario")
    public ResponseEntity<Object> getScenariosByStoryId(@RequestParam("storyId") int storyId){
        List<Map<String, Object>> scenarios = dbHandler.getScenariosByStoryId(storyId);
        if(scenarios == null){
            return new ResponseEntity<>("Errore nella ricerca degli scenari", HttpStatusCode.valueOf(400));
        }
        else
            return new ResponseEntity<>(scenarios, HttpStatus.OK);
    }
*//*


    @GetMapping("/links")
    public ResponseEntity<Object> getLinksByStoryId(@RequestParam("storyId") int storyId){
        List<Map<String, Object>> links = dbHandler.getLinksByStoryId(storyId);
        if(links == null){
            return new ResponseEntity<>("Errore nella ricerca degli scenari", HttpStatusCode.valueOf(400));
        }
        else
            return new ResponseEntity<>(links, HttpStatus.OK);

    }

}
*/


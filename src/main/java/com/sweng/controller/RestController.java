package com.sweng.controller;

import com.sweng.entity.*;
import com.sweng.utilities.ElementService;
import com.sweng.utilities.ScenarioService;
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
        @Autowired
        private ElementService elementService;
        @Autowired
        private ScenarioService scenarioService;

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
         * @return response entity con codice 200 e l'utente nel body se l'inserimento è andato a buon fine
         * response entity 400 con messaggio di errore se si verifica un errore nell'inserimento
         **/
        @PostMapping("/user")
        public ResponseEntity<Object> postUser(@RequestBody User user) {
            return userService.saveUser(user);
        }

        /**
         * ritorna tutti le storie create dagli utenti
         * @return una lista di storie se la richiesta va a buon fine.
         + Ritorna codice 400 se c'è un errore nel recupero delle storie
         **/
        @GetMapping("/story")
        public ResponseEntity<Object> getStories(){
            List<Story> stories = storyService.getStories();

            if(stories == null)
                return new ResponseEntity<>("Errore nel recupero delle storie", HttpStatusCode.valueOf(400));
            else
                return new ResponseEntity<>(stories, HttpStatus.OK);
        }

        /**
         * Crea una nuova storia
         *
         *
         * @param story: la storia da creare
         * @return Response entity con la storia nel body e codice 200 se la creazione va a buon fine.
         * Response entity con errore e codice 400 se c'è un errore nel salvataggio della storia
         */
        @PostMapping("/story")
        public ResponseEntity<Object> postStory(@RequestBody Story story){

            try {
                storyService.createStory(story);
                return new ResponseEntity<>(story, HttpStatus.OK);
            } catch (DataAccessException e) {
                return new ResponseEntity<>("Errore nel salvataggio della storia", HttpStatusCode.valueOf(400));
            }
        }

        /**
         *
         * @param name: il nome dell'oggetto da creare
         * @param description: la descrizione dell'oggetto da creare
         * @return Response entity con oggetto nel body e codice 200 se la creazione va a buon fine.
         * Response entity con errore e codice 400 se c'è un errore nella creazione dell'oggetto
         */
        @PostMapping("/object")
        public ResponseEntity<Object> postObject(@RequestBody String name, @RequestBody String description){

            try {
                StoryObject storyObject = new StoryObject(name, description);
                elementService.createObject(storyObject);

                return new ResponseEntity<>(storyObject, HttpStatus.OK);
            }
            catch(Exception e){
                return new ResponseEntity<>("Errore nella creazione dell'oggetto", HttpStatusCode.valueOf(400));
            }

        }

        /**
         *
         * @param scenario: lo scenario da creare
         * @return Response entity con scenario nel body e codice 200 se la creazione va a buon fine.
         * Response entity con errore e codice 400 se c'è un errore nella creazione dello scenario
         */
        @PostMapping("/scenario")
        public ResponseEntity<Object> postScenario(@RequestBody Scenario scenario){
            return scenarioService.createScenario(scenario);
        }

        /**
         *
         * @param storyId: l'id della storia a cui apppartengono gli scenari da cercare
         * @return Response entity con gli scenari e codice 200 se la ricerca va a buon fine
         * Response entity con errore e codice 400 se c'è un errore nella ricerca
         */
        @GetMapping("/scenario")
        public ResponseEntity<Object> getScenariosByStoryId(@RequestParam("storyId") int storyId){
            List<Scenario> scenarios = scenarioService.getScenariosByStoryId(storyId);
            if(scenarios == null){
                return new ResponseEntity<>("Errore nella ricerca degli scenari", HttpStatusCode.valueOf(400));
            }else
                return new ResponseEntity<>(scenarios, HttpStatus.OK);
        }

        /**
         *
         * @param riddle: l'indovinello da creare
         * @return Response entity con l'indovinello e codice 200 se la creazione va a buon fine
         * Response entity con errore e codice 400 se c'è un errore nella creazione
         */
        @PostMapping("/riddle")
        public ResponseEntity<Object> postRiddle(@RequestBody Riddle riddle){
            return elementService.createRiddle(riddle);
        }

        /**
         *
         * @param storyId: l'id della storia a cui appartengono i collegamenti da cercare
         * @return Response entity con i collegamenti e codice 200 se la ricerca va a buon fine
         * Response entity con errore e codice 400 se c'è un errore nella ricerca
         */
        @GetMapping("/links")
        public ResponseEntity<Object> getLinksByStoryId(@RequestParam("storyId") int storyId){
            List<Map<String, Object>> links = storyService.getLinksByStoryId(storyId);
            if(links == null){
                return new ResponseEntity<>("Errore nella ricerca degli scenari", HttpStatusCode.valueOf(400));
            }else
                return new ResponseEntity<>(links, HttpStatus.OK);
        }

    }
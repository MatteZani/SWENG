package com.sweng;

import com.sweng.entity.Riddle;
import com.sweng.entity.Scenario;
import com.sweng.entity.Story;
import com.sweng.entity.User;
import com.sweng.utilities.DBHandler;
import jakarta.websocket.server.PathParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api/home")
public class RestController {


    @Autowired
    private DBHandler dbHandler;

    Logger logger = LoggerFactory.getLogger(RestController.class);

    @GetMapping("/user")
    public ResponseEntity<List<Map<String, Object>>> getUsers() {
        List<Map<String, Object>> users = dbHandler.getUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

//    @GetMapping("/user/username")
//    public ResponseEntity<Object> getUserByName(@RequestParam String username){
////        if(username.equals("pippo")){
////            User pippo = new User("pippo", "pippo");
////            return new ResponseEntity<>(pippo, HttpStatus.OK);
////        }
////        else{
////            return new ResponseEntity<>("Utente non trovato", HttpStatus.NOT_FOUND);
////        }
//    }

    /**
     * inserisce un nuovo utente nel database
     * @param user l'utente da inserire
     * @return response entity con codice 200 e l'utente nel body se l'inserimento è andato a buon fide
     * response entity 400 con messaggio di errore se si verifica un errore nell'inserimento
     *
     **/
    @PostMapping("/user")
    public ResponseEntity<Object> postUser(@RequestBody User user){

        return dbHandler.saveUser(user);
    }

//    @GetMapping("/story")
//    public ResponseEntity<Object> getStories(){
//
//    }

    @PostMapping("/story")
    public ResponseEntity<Object> postStory(@RequestBody Story story){

        try {
            dbHandler.createStory(story);
            return new ResponseEntity<>(story, HttpStatus.OK);
        } catch (DataAccessException e) {
            return new ResponseEntity<>("Errore nel salvataggio della storia", HttpStatusCode.valueOf(400));
        }
    }

    @PostMapping("/object")
    public ResponseEntity<Object> postObject(@RequestBody String name){

        return dbHandler.createObject(name);
    }

    @PostMapping("/scenario")
    public ResponseEntity<Object> postScenario(@RequestBody Scenario scenario){

        return dbHandler.createScenario(scenario);
    }

    @PostMapping("/riddle")
    public ResponseEntity<Object> postRiddle(@RequestBody Riddle riddle){

        return dbHandler.createRiddle(riddle);
    }

    @GetMapping("/scenario")
    public ResponseEntity<Object> getScenariosByStoryId(@RequestParam("storyId") int storyId){
        return dbHandler.getScenariosByStoryId(storyId);
    }

}

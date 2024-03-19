package com.sweng;

import com.sweng.entity.Riddle;
import com.sweng.entity.Scenario;
import com.sweng.entity.Story;
import com.sweng.entity.User;
import com.sweng.utilities.DBHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api/home")
public class RestController {


    @Autowired
    private DBHandler dbHandler;

    Logger logger = LoggerFactory.getLogger(RestController.class);

    @GetMapping("/user")
    public ResponseEntity<List<User>> getUsers(){

        ArrayList<User> users = dbHandler.getUsers();
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

        return dbHandler.createStory(story);
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


}

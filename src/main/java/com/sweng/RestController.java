package com.sweng;

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

    @GetMapping("")
    public ResponseEntity<String> helloWorld(){
        return new ResponseEntity<>("Hello World", HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<List<User>> getUsers(){
        User pippo = new User("pippo", "pippo");
        User pluto = new User("pluto", "pluto");

        List<User> users = new ArrayList<>();
        users.add(pippo);
        users.add(pluto);

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<User> getUserByName(@RequestParam String username){
        if(username.equals("pippo")){
            User pippo = new User("pippo", "pippo");
            return new ResponseEntity<User>(pippo, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/user")
    public ResponseEntity<User> postUser(@RequestBody User user){

        return dbHandler.saveUser(user);
    }


}

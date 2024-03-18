package com.sweng;

import com.sweng.entity.User;
import com.sweng.utilities.DBHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api/home")
public class RestController {


    @Autowired
    private DBHandler dbHandler;

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

    @GetMapping("/user/prova")
    public ResponseEntity<User> getUser(@RequestParam String username){
        if(username.equals("pippo")){
            User pippo = new User("pippo", "pippo");
            return new ResponseEntity<User>(pippo, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<String> postUser(@RequestBody User user){
        System.out.println("sono qui");

        return dbHandler.saveUser(user);
    }


}

package com.sweng;


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


    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/menu")
    public String menu(Model model){
        return "menu";
    }

    @GetMapping("/login/process")
    public String processLogin(@RequestParam("username") String username, @RequestParam("password") String password, Model model){
        //TODO controllare se l'utente è correttamente registrato

        return "catalogo";
    }

    @GetMapping("/registration")
    public String registration(Model model){
        return "registrazione";
    }


    @PostMapping("/registration/process")
    public String processRegistration(@RequestParam("username") String username, @RequestParam("password") String password, Model model) {
        User user = new User(username, password);
        dbHandler.saveUser(user);

        // Reindirizzamento alla pagina "avvenuta registrazione"
        return "homepage";
    }


}
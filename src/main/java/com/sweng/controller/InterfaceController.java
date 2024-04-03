package com.sweng.controller;


import com.sweng.utilities.ScenarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InterfaceController {

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private ScenarioService scenarioservice;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @GetMapping("/menu")
    public String menu(Model model){
        String username = (String) httpSession.getAttribute("username");
        if(username != null){
            model.addAttribute("username", username);
            return "homepage";
        }

        return "menu";
    }

    @GetMapping("/play")
    public String play(Model model){
        return "play";
    }


}
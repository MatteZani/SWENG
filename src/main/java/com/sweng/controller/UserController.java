package com.sweng.controller;

import com.sweng.entity.User;
import com.sweng.utilities.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    @Autowired
    private HttpSession httpSession;
    @Autowired
    private UserService userService;

    @GetMapping("/menu")
    public String menu(Model model){
        String username = (String) httpSession.getAttribute("username");
        if(username != null){
            model.addAttribute("username", username);
            return "homepage";
        }
        return "menu";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login/process")
    public String processLogin(@RequestParam("username") String username, @RequestParam("password") String password,
                               Model model) {
        boolean isValidCredentials = userService.verifyCredentials(username, password);
        if (isValidCredentials) {
            httpSession.setAttribute("username", username);
            httpSession.setAttribute("password", password);

            model.addAttribute("username", username);
            return "homepage";
        } else {
            // Reindirizza alla pagina di login con un messaggio di errore se le credenziali non sono valide
            model.addAttribute("error", "Credenziali non valide");
            return "login";
        }
    }

    @GetMapping("/registration")
    public String registration(Model model){
        return "registration";
    }


    @PostMapping("/registration/process")
    public String processRegistration(@RequestParam("username") String username, @RequestParam("password") String password, Model model) {
        User user = new User(username, password);
        userService.saveUser(user);

        httpSession.setAttribute("username", username);
        httpSession.setAttribute("password", password);

        model.addAttribute("username", username);
        return "homepage";
    }

    @GetMapping ("/logout")
    public String logout() {
        httpSession.removeAttribute("username");
        httpSession.removeAttribute("password");

        return "menu";
    }

    @GetMapping("/home")
    public String home(Model model){
        String username = (String) httpSession.getAttribute("username");
        if(username == null){
            return "login";
        }
        model.addAttribute("username", username);
        return "homepage";
    }


}

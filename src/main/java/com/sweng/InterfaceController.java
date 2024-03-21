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

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "index";
    }

    @GetMapping("/login")
    public String login() {



//        try {
//            // Connessione al database
//            conn = DriverManager.getConnection(DB_URL, USER, PASS);
//
//            // Query per verificare le credenziali
//            String sql = "SELECT COUNT(*) FROM CREDENZIALI WHERE USERNAME=? AND PASSWORD=?";
//            stmt = conn.prepareStatement(sql);
//
//            jdbcTemplate.update(sql, )
//
//            if (rs.next()) {
//                // Le credenziali sono corrette, reindirizza alla home o ad un'altra pagina
//                return "redirect:/home";
//            } else {
//                // Le credenziali sono errate, mostra un messaggio di errore
//                model.addAttribute("error", "Credenziali non valide. Riprova.");
//                return "login"; // Torna alla pagina di login
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            // Gestione degli errori
//            model.addAttribute("error", "Si è verificato un errore durante la verifica delle " +
//                    "credenziali. Riprova più tardi.");
//            return "login"; // Torna alla pagina di login
//        } finally {
//            try {
//                // Chiusura delle risorse
//                if (rs != null) rs.close();
//                if (stmt != null) stmt.close();
//                if (conn != null) conn.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }

        return "login";
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

    @GetMapping("/registration/process")
    public String processRegistration(@RequestParam("username") String username, @RequestParam("password") String password, Model model){
        User user = new User(username, password);
        dbHandler.saveUser(user);

        //return "catalogo";
        // Reindirizzamento alla pagina "avvenuta registrazione"
        return "redirect:/homepage.html";

    }
    /*@PostMapping("/registration/process")
    public String processRegistration(@RequestParam("username") String username, @RequestParam("password") String password, Model model) {
        User user = new User(username, password);
        dbHandler.saveUser(user);
        //return "redirect:/catalogo";

        // Reindirizzamento alla pagina "avvenuta registrazione"
        return "redirect:/homepage.html";
    }*/


}
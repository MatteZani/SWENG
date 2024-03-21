package com.sweng;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.*;

@Controller
public class InterfaceController {

    // URL di connessione al database
    static final String DB_URL = "jdbc:mysql://localhost:3306/nome_database";
    static final String USER = "SWENG";
    static final String PASS = "ProgettiGiga";

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "index";
    }

    @PostMapping("/login")
    public String login(@RequestParam(name="username") String username, @RequestParam(name="password") String password,
                        Model model) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Connessione al database
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // Query per verificare le credenziali
            String sql = "SELECT * FROM CREDENZIALI WHERE USERNAME=? AND PASSWORD=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            rs = stmt.executeQuery();

            if (rs.next()) {
                // Le credenziali sono corrette, reindirizza alla home o ad un'altra pagina
                return "redirect:/home";
            } else {
                // Le credenziali sono errate, mostra un messaggio di errore
                model.addAttribute("error", "Credenziali non valide. Riprova.");
                return "login"; // Torna alla pagina di login
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gestione degli errori
            model.addAttribute("error", "Si è verificato un errore durante la verifica delle " +
                    "credenziali. Riprova più tardi.");
            return "login"; // Torna alla pagina di login
        } finally {
            try {
                // Chiusura delle risorse
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}

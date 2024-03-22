package com.sweng.utilities;

import com.sweng.entity.Riddle;
import com.sweng.entity.Scenario;
import com.sweng.entity.Story;
import com.sweng.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DBHandler {

    Logger logger = LoggerFactory.getLogger(DBHandler.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> getUsers() {
        return jdbcTemplate.queryForList("SELECT * FROM CREDENZIALI");
    }

    public ResponseEntity<Object> saveUser(User user){
        try {
            String sql = "INSERT INTO CREDENZIALI (USERNAME, PASSWORD) VALUES (?, ?)";
            jdbcTemplate.update(sql, user.getUsername(), user.getPassword());
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (DataAccessException e) {
            logger.error("Lanciata DataAccessException nel metodo postUser della classe DBHandler. Causa dell'eccezione: {}. Descrizione dell'eccezione: {}", e.getCause(), e.getMessage());
            return new ResponseEntity<>("Errore nel salvataggio dei dati", HttpStatus.valueOf(400));
        }
    }

    public ResponseEntity<Object> createStory(Story story){
        try {
            String sql = "INSERT INTO STORIE(TITOLO, TRAMA, GENERE, ID_CREATORE) VALUES (?, ?, ?, ?)";
            jdbcTemplate.update(sql, story.getTitle(), story.getPlot(), story.getCategory(), story.getCreatorId());
            return new ResponseEntity<>(story, HttpStatus.OK);
        } catch (DataAccessException e) {
            logger.error("Lanciata eccezione nel metodo createStory della classe DBHandler. Causa dell'eccezione: {}. Descrizione dell'eccezione: {}",  e.getCause(), e.getMessage());
            return new ResponseEntity<>("Errore nel salvataggio dei dati", HttpStatus.valueOf(400));
        }
    }

    public ResponseEntity<Object> createObject(String name){
        try {
            String sql = "INSERT INTO OGGETTI(NOME) VALUES (?)";
            jdbcTemplate.update(sql, name);
            return new ResponseEntity<>(name, HttpStatus.OK);
        } catch (DataAccessException e) {
            logger.error("Lanciata eccezione nel metodo createObject della classe DBHandler. Causa dell'eccezione: {}. Descrizione dell'eccezione: {}",  e.getCause(), e.getMessage());
            return new ResponseEntity<>("Errore nel salvataggio dei dati", HttpStatus.valueOf(400));
        }
    }

    public ResponseEntity<Object> createScenario(Scenario scenario){
        try {
            String sql = "INSERT INTO SCENARI(DESCRIZIONE,ID_STORIA) VALUES (?,?)";
            jdbcTemplate.update(sql, scenario.getDescription(), scenario.getStoryId() );
            return new ResponseEntity<>(scenario, HttpStatus.OK);
        } catch (DataAccessException e) {
            logger.error("Lanciata eccezione nel metodo createScenario della classe DBHandler. Causa dell'eccezione: {}. Descrizione dell'eccezione: {}",  e.getCause(), e.getMessage());
            return new ResponseEntity<>("Errore nel salvataggio dei dati", HttpStatus.valueOf(400));
        }
    }
    public ResponseEntity<Object> createRiddle(Riddle riddle){
        try {
            String sql = "INSERT INTO INDOVINELLI(DOMANDA, RISPOSTA) VALUES (?,?)";
            jdbcTemplate.update(sql, riddle.getQuestion(), riddle.getAnswer() );
            return new ResponseEntity<>(riddle, HttpStatus.OK);
        } catch (DataAccessException e) {
            logger.error("Lanciata eccezione nel metodo createRiddle della classe DBHandler. Causa dell'eccezione: {}. Descrizione dell'eccezione: {}",  e.getCause(), e.getMessage());
            return new ResponseEntity<>("Errore nel salvataggio dei dati", HttpStatus.valueOf(400));
        }
    }

    public boolean verifyCredentials(String username, String password) {
        String sql = "SELECT COUNT(*) FROM CREDENZIALI WHERE USERNAME = ? AND PASSWORD = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, username, password);
        return count > 0;
    }

    // Metodo per aggiungere uno scenario alla storia nel database
    public void addScenarioToStory(int storyId, String description) {
        String sql = "INSERT INTO SCENARI (DESCRIZIONE, ID_STORIA) VALUES (?, ?)";
        jdbcTemplate.update(sql, description, storyId);
    }



}

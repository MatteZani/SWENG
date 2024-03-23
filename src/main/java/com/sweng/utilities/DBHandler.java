package com.sweng.utilities;

import com.sweng.entity.Riddle;
import com.sweng.entity.Scenario;
import com.sweng.entity.Story;
import com.sweng.entity.User;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.*;

@Service
public class DBHandler {

    @Autowired
    private HttpSession httpSession;

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

    public void createStory(Story story){
        try {
            String sql = "INSERT INTO STORIE(TITOLO, TRAMA, GENERE, CREATORE, SCENARIO_INIZIALE) VALUES (?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql, story.getTitle(), story.getPlot(), story.getCategory(), story.getCreator(), null);
            String maxId = "SELECT MAX(ID) FROM STORIE";
            int currentStoryId = jdbcTemplate.queryForObject(maxId, Integer.class);
            httpSession.setAttribute("currentStoryId", currentStoryId);

        } catch (DataAccessException e) {
            logger.error("Lanciata eccezione nel metodo createStory della classe DBHandler. Causa dell'eccezione: {}. Descrizione dell'eccezione: {}",  e.getCause(), e.getMessage());
            throw e;
        }
    }

/*    public List<Story> getStories(){
        try{

            return jdbcTemplate.queryForObject("SELECT * FROM STORIE", Story.class);


        } catch (DataAccessException e) {
            logger.error("Lanciata eccezione nel metodo getStories della classe DBHandler. Causa dell'eccezione: {}. Descrizione dell'eccezione: {}",  e.getCause(), e.getMessage());
            return new ArrayList<Story>();

        }
    }*/

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

    public void createScenario(int storyId, String description){
        try {
            String sql = "INSERT INTO SCENARI (DESCRIZIONE, ID_STORIA) VALUES (?, ?)";
            jdbcTemplate.update(sql, description, storyId);


            httpSession.setAttribute("currentScenarioId", this.getMaxScenarioId());

        }
        catch (DataAccessException e) {
            logger.error("Lanciata eccezione nel metodo createScenario della classe DBHandler. Causa dell'eccezione: {}. Descrizione dell'eccezione: {}",  e.getCause(), e.getMessage());
        }

    }

    public int getMaxScenarioId(){

        try {
            String getScenarioId = "SELECT MAX(ID) FROM SCENARI";
            int maxScenarioId = jdbcTemplate.queryForObject(getScenarioId, Integer.class);

            return maxScenarioId;
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

    }

    // Metodo per aggiungere uno scenario alla storia nel database
    public void addScenarioToStory(int storyId) {
        try {
            String addScenario = "UPDATE STORIE SET SCENARIO_INIZIALE = ? WHERE ID = ?";
            jdbcTemplate.update(addScenario, httpSession.getAttribute("currentScenarioId"), storyId);
        } catch (DataAccessException e) {
            logger.error("Lanciata eccezione nel metodo addScenarioToStory della classe DBHandler. Causa dell'eccezione: {}. Descrizione dell'eccezione: {}",  e.getCause(), e.getMessage());
        }
    }


    public List<Map<String, Object>> getScenariosByStoryId(int storyId){
        try{
            return jdbcTemplate.queryForList("SELECT * FROM SCENARI WHERE ID_STORIA = ?", storyId);

        } catch (DataAccessException e) {
            logger.error("Lanciata eccezione nel metodo getScenariosByStoryId della classe DBHandler. Causa dell'" +
                    "eccezione: {}. Descrizione dell'eccezione: {}",  e.getCause(), e.getMessage());
            return null;
        }
    }

    public void connectScenarios(int start, int end, int story){
        String sql = "INSERT INTO COLLEGAMENTI(SCENARIO_PARTENZA, SCENARIO_ARRIVO, STORIA_APPARTENENZA, DESCRIZIONE) VALUES (?, ?, ?, 'Collegamento di prova')";

        jdbcTemplate.update(sql, start, end, story);
    }

    public List<Map<String, Object>> getLinksByStoryId(int storyId){
        try{
            return jdbcTemplate.queryForList("SELECT * FROM COLLEGAMENTI WHERE STORIA_APPARTENENZA = ?", storyId);

        } catch (DataAccessException e) {
            logger.error("Lanciata eccezione nel metodo getLinksByStoryId della classe DBHandler. Causa dell'" +
                    "eccezione: {}. Descrizione dell'eccezione: {}",  e.getCause(), e.getMessage());
            return null;
        }
    }



}

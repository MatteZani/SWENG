package com.sweng.utilities;

import com.sweng.entity.Riddle;
import com.sweng.entity.StoryObject;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class ElementService {

    @Autowired
    private HttpSession httpSession;

    Logger logger = LoggerFactory.getLogger(ElementService.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int createObject(StoryObject storyObject) {
        try {
            String sql = "INSERT INTO OGGETTI(NOME, DESCRIZIONE, ID_STORIA) VALUES (?, ?, ?)";
            jdbcTemplate.update(sql, storyObject.getName(), storyObject.getDescription(), httpSession.getAttribute("currentStoryId"));
            String maxIdQuery = "SELECT MAX(ID) FROM OGGETTI";
            int maxId = jdbcTemplate.queryForObject(maxIdQuery, Integer.class);
            return maxId;
        } catch (DataAccessException e) {
            logger.error("Lanciata eccezione nel metodo createObject della classe DBHandler. Causa dell'eccezione: {}. Descrizione dell'eccezione: {}", e.getCause(), e.getMessage());
            return -1;
        }
    }

    public ResponseEntity<Object> createRiddle(Riddle riddle) {
        try {
            String sql = "INSERT INTO INDOVINELLI(DOMANDA, RISPOSTA) VALUES (?,?)";
            jdbcTemplate.update(sql, riddle.getQuestion(), riddle.getAnswer());
            return new ResponseEntity<>(riddle, HttpStatus.OK);
        } catch (DataAccessException e) {
            logger.error("Lanciata eccezione nel metodo createRiddle della classe DBHandler. Causa dell'eccezione: {}. Descrizione dell'eccezione: {}", e.getCause(), e.getMessage());
            return new ResponseEntity<>("Errore nel salvataggio dei dati", HttpStatus.valueOf(400));
        }
    }


    public int getMaxObjectId() {
        try {
            String getScenarioId = "SELECT MAX(ID) FROM OGGETTI";
            int maxObjectId = jdbcTemplate.queryForObject(getScenarioId, Integer.class);

            return maxObjectId;
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        } catch (NullPointerException e) {
            logger.error("NullPointerException nel metodo getMaxObjectId della classe DBHandler");
            return 0;
        }
    }

    public int getMaxRiddleId(){
        try {
            String getScenarioId = "SELECT MAX(ID) FROM INDOVINELLI";
            int maxObjectId = jdbcTemplate.queryForObject(getScenarioId, Integer.class);

            return maxObjectId;
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        } catch (NullPointerException e) {
            logger.error("NullPointerException nel metodo getMaxRiddleId della classe ElementService");
            return 0;
        }

    }



}

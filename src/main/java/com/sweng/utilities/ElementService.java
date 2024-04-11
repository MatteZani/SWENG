package com.sweng.utilities;

import com.sweng.entity.Riddle;
import com.sweng.entity.StoryObject;
import com.sweng.mapper.RiddleRowMapper;
import com.sweng.mapper.StoryObjectRowMapper;
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
            logger.error("Lanciata eccezione nel metodo createObject della classe ElementService. Causa dell'eccezione: {}. Descrizione dell'eccezione: {}", e.getCause(), e.getMessage());
            return -1;
        }
    }

    public StoryObject getStoryObjectById(int storyObjectId){
        String sql = "SELECT * FROM OGGETTI WHERE ID = ?";
        StoryObject storyObject = jdbcTemplate.queryForObject(sql, new StoryObjectRowMapper(), storyObjectId);

        return storyObject;
    }

    public ResponseEntity<Object> createRiddle(Riddle riddle) {
        try {
            String sql = "INSERT INTO INDOVINELLI(DOMANDA, RISPOSTA) VALUES (?,?)";
            jdbcTemplate.update(sql, riddle.getQuestion(), riddle.getAnswer());
            return new ResponseEntity<>(riddle, HttpStatus.OK);
        } catch (DataAccessException e) {
            logger.error("Lanciata eccezione nel metodo createRiddle della classe ElementService. Causa dell'eccezione: {}. Descrizione dell'eccezione: {}", e.getCause(), e.getMessage());
            return new ResponseEntity<>("Errore nel salvataggio dei dati", HttpStatus.valueOf(400));
        }
    }

    public Riddle getRiddleById(int riddleId){
        String sql = "SELECT * FROM INDOVINELLI WHERE ID = ?";
        Riddle riddle = jdbcTemplate.queryForObject(sql, new RiddleRowMapper(), riddleId);

        return riddle;
    }

    public int getMaxObjectId() {
        try {
            String getScenarioId = "SELECT MAX(ID) FROM OGGETTI";
            int maxObjectId = jdbcTemplate.queryForObject(getScenarioId, Integer.class);

            return maxObjectId;
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        } catch (NullPointerException e) {
            logger.error("NullPointerException nel metodo getMaxObjectId della classe ElementService");
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

    public boolean checkObjectInInventory(String username, int objectId) {
        String sql = "SELECT COUNT(*) FROM INVENTARIO WHERE USERNAME = ? AND ID_OGGETTO = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, username, objectId);
        return count > 0;
    }

    public void addObjectToInventory(int storyId, int objectId, String username) {
        String checkSql = "SELECT COUNT(*) FROM INVENTARIO WHERE USERNAME = ? AND ID_STORIA = ?";
        int count = jdbcTemplate.queryForObject(checkSql, Integer.class, username, storyId);

        if (count == 0) {
            String sql = "INSERT INTO INVENTARIO (USERNAME, ID_STORIA, ID_OGGETTO) VALUES (?, ?, ?)";
            jdbcTemplate.update(sql, username, storyId, objectId);
        }

    }

    public void deleteObjectFromInventory(Integer storyId, String username){
        String sql = "DELETE FROM INVENTARIO WHERE ID_STORIA = ? AND USERNAME = ?";
        jdbcTemplate.update(sql, storyId, username);
    }


}
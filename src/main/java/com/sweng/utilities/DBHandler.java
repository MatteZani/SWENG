package com.sweng.utilities;

import com.sweng.RestController;
import com.sweng.entity.Story;
import com.sweng.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DBHandler {

    Logger logger = LoggerFactory.getLogger(DBHandler.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    public ArrayList<User> getUsers(){
        String sql = "SELECT * FROM CREDENZIALI";
        //TODO tornare una response entity che contiene come body le informazioni relative agli user
        return new ArrayList<>();
    }

    public ResponseEntity<Object> saveUser(User user){
        try {
            String sql = "INSERT INTO CREDENZIALI (USERNAME, PASSWORD) VALUES (?, ?)";
            jdbcTemplate.update(sql, user.getUsername(), user.getPassword());
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (DataAccessException e) {
            logger.error("Lanciata eccezione nel metodo postUser della classe DBHandler. Causa dell'eccezione: {}. Descrizione dell'eccezione: {}", e.getCause(), e.getMessage());
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
}

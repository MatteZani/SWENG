package com.sweng.utilities;

import com.sweng.RestController;
import com.sweng.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Service
public class DBHandler {

    Logger logger = LoggerFactory.getLogger(DBHandler.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    public ResponseEntity<Object> saveUser(User user){
        try {
            String sql = "INSERT INTO CREDENZIALI (USERNAME, PASSWORD) VALUES (?, ?)";
            jdbcTemplate.update(sql, user.getUsername(), user.getPassword());
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (DataAccessException e) {
            logger.error("Lanciata eccezione di tipo {}. Causa dell'eccezione: {}. Descrizione dell'eccezione: {}", e.getClass(), e.getCause(), e.getMessage());
            return new ResponseEntity<>("Errore nel salvataggio dei dati", HttpStatus.valueOf(400));
        }




    }
}

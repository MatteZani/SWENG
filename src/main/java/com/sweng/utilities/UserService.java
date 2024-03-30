package com.sweng.utilities;

import com.sweng.entity.User;
import com.sweng.mapper.UserRowMapper;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    Logger logger = LoggerFactory.getLogger(UserService.class);

    public List<Map<String, Object>> getUsers() {
        return jdbcTemplate.queryForList("SELECT * FROM CREDENZIALI");
    }

    public ResponseEntity<Object> saveUser(User user) {
        try {
            String sql = "INSERT INTO CREDENZIALI (USERNAME, PASSWORD) VALUES (?, ?)";
            jdbcTemplate.update(sql, user.getUsername(), user.getPassword());
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (DataAccessException e) {
            logger.error("Lanciata DataAccessException nel metodo postUser della classe DBHandler. Causa dell'eccezione: {}. Descrizione dell'eccezione: {}", e.getCause(), e.getMessage());
            return new ResponseEntity<>("Errore nel salvataggio dei dati", HttpStatus.valueOf(400));
        }
    }

    public boolean verifyCredentials(String username, String password) {
        String sql = "SELECT COUNT(*) FROM CREDENZIALI WHERE USERNAME = ? AND PASSWORD = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, username, password);
        return count > 0;
    }

    public User getUserByUsername(String username){
        String sql = "SELECT * FROM CREDENZIALI WHERE USERNAME = ?";
        User user = jdbcTemplate.queryForObject(sql, new UserRowMapper(), username);

        return user;
    }
}

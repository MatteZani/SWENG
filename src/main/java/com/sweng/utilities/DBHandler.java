package com.sweng.utilities;

import com.sweng.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class DBHandler {

    public ResponseEntity<String> saveUser(User user){
        //salvo utente
        return new ResponseEntity<>("Utente salvato", HttpStatus.OK);

    }
}

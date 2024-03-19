CREATE DATABASE IF NOT EXISTS SWENG;
USE SWENG;
CREATE TABLE IF NOT EXISTS CREDENZIALI(
	ID INT AUTO_INCREMENT NOT NULL,
    USERNAME VARCHAR(20) NOT NULL,
    PASSWORD VARCHAR(20) NOT NULL,
    
    PRIMARY KEY(ID)
)
ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS STORIE(
    ID INT AUTO_INCREMENT NOT NULL,
    TITOLO VARCHAR(20) NOT NULL,
    TRAMA VARCHAR(2000) NOT NULL,
    GENERE VARCHAR(20) NOT NULL CHECK (GENERE IN ('Horror', 'Avventura', 'Comica', 'Fantasy','Drammatica')),
    PRIMARY KEY(ID)
)
ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS OGGETTI(
	ID INT AUTO_INCREMENT NOT NULL,
    NOME VARCHAR(20) NOT NULL,
    
    PRIMARY KEY(ID)
)
ENGINE = INNODB;

SELECT * FROM CREDENZIALI;
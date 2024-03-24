CREATE DATABASE IF NOT EXISTS SWENG;
USE SWENG;
CREATE TABLE IF NOT EXISTS CREDENZIALI(
	ID INT DEFAULT 0 NOT NULL,
    USERNAME VARCHAR(20) NOT NULL,
    PASSWORD VARCHAR(20) NOT NULL,
    
    PRIMARY KEY(USERNAME)
)
ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS STORIE(
	ID INT AUTO_INCREMENT NOT NULL,
    TITLE VARCHAR(20) NOT NULL,
    PLOT VARCHAR(2000) NOT NULL,
    CATEGORY VARCHAR(20) NOT NULL CHECK (CATEGORY IN ('Horror', 'Azione','Fantasy','Comica', 'Drammatica')),
	CREATOR VARCHAR(20) NOT NULL REFERENCES CREDENZIALI.USERNAME,
    INITIAL_SCENARIO INT DEFAULT NULL REFERENCES SCENARIO.ID ,
    PRIMARY KEY(ID)
)
ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS SCENARI(
	ID INT AUTO_INCREMENT NOT NULL,
    DESCRIZIONE VARCHAR(2000) NOT NULL,
    ID_STORIA INT NOT NULL REFERENCES STORIE.ID,
    ID_OGGETTO INT DEFAULT 0 REFERENCES OGGETTI.ID,
    
    PRIMARY KEY(ID)
)
ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS OGGETTI(
	ID INT AUTO_INCREMENT NOT NULL,
    NOME VARCHAR(20) NOT NULL,
    DESCRIZIONE VARCHAR(200),
    
    PRIMARY KEY(ID)
)
ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS INDOVINELLI(
	ID INT AUTO_INCREMENT NOT NULL,
    DOMANDA VARCHAR(2000) NOT NULL,
    RISPOSTA VARCHAR(2000) NOT NULL,
    
    PRIMARY KEY(ID)
)
ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS COLLEGAMENTI(
	SCENARIO_PARTENZA INT NOT NULL DEFAULT 0 REFERENCES SCENARI.ID,
    SCENARIO_ARRIVO INT NOT NULL DEFAULT 1 REFERENCES SCENARI.ID,
    STORIA_APPARTENENZA INT NOT NULL DEFAULT 2 REFERENCES STORIE.ID,
    DESCRIZIONE VARCHAR(200),
    
    PRIMARY KEY(SCENARIO_PARTENZA, SCENARIO_ARRIVO)
)
ENGINE = INNODB;

-- -------------------------------------------------
SELECT * FROM CREDENZIALI;
SELECT * FROM STORIE;
SELECT * FROM SCENARI;
SELECT * FROM COLLEGAMENTI;
SELECT * FROM OGGETTI;
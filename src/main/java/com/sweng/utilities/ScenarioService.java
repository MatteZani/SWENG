package com.sweng.utilities;

import com.sweng.entity.Scenario;
import com.sweng.entity.ScenarioBuilder;
import com.sweng.mapper.ScenarioRowMapper;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ScenarioService {

    @Autowired
    private HttpSession httpSession;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    Logger logger = LoggerFactory.getLogger(ScenarioService.class);

    public ResponseEntity<Object> createScenario(Scenario scenario) {
        try {
            String sql = "INSERT INTO SCENARI(DESCRIZIONE,ID_STORIA) VALUES (?,?)";
            jdbcTemplate.update(sql, scenario.getDescription(), scenario.getStoryId());
            return new ResponseEntity<>(scenario, HttpStatus.OK);
        } catch (DataAccessException e) {
            logger.error("Lanciata eccezione nel metodo createScenario della classe ScenarioService. Causa dell'eccezione: {}. Descrizione dell'eccezione: {}", e.getCause(), e.getMessage());
            return new ResponseEntity<>("Errore nel salvataggio dei dati", HttpStatus.valueOf(400));
        }
    }


    public Scenario createScenario(int storyId, String description, int necessaryObjectId, int foundObjectId, int riddleId) {
        try {
            String sql = "INSERT INTO SCENARI (DESCRIZIONE, ID_STORIA, ID_OGGETTO_NECESSARIO, ID_OGGETTO_OTTENUTO, ID_INDOVINELLO) VALUES (?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql, description, storyId, necessaryObjectId, foundObjectId, riddleId);

            Scenario scenario = new ScenarioBuilder()
                    .setId(this.getMaxScenarioId())
                    .setDescription(description)
                    .setStoryId(storyId)
                    .setNecessaryObjectId(necessaryObjectId)
                    .setFoundObjectId(foundObjectId)
                    .setRiddleId(riddleId)
                    .build();

            httpSession.setAttribute("currentScenarioId", this.getMaxScenarioId());

            return scenario;

        } catch (DataAccessException e) {
            logger.error("Lanciata eccezione nel metodo createScenario della classe ScenarioService. Causa dell'eccezione: {}. Descrizione dell'eccezione: {}", e.getCause(), e.getMessage());
            return new Scenario();
        }

    }

    public Scenario getScenarioById(Integer scenarioId) {
        String sql = "SELECT * FROM SCENARI WHERE ID = ?";
        Scenario scenario = jdbcTemplate.queryForObject(sql, new ScenarioRowMapper(), scenarioId);

        return scenario;
    }

    public int getMaxScenarioId() {

        try {
            String getScenarioId = "SELECT MAX(ID) FROM SCENARI";
            int maxScenarioId = jdbcTemplate.queryForObject(getScenarioId, Integer.class);

            return maxScenarioId;
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }catch(NullPointerException e){
            logger.error("NullPointerException nel metodo getMaxScenarioId della classe ScenarioService");
            return 1;
        }

    }

    public ArrayList<Scenario> getScenariosByStoryId(int storyId) {
        try {
            return (ArrayList<Scenario>) jdbcTemplate.query("SELECT * FROM SCENARI WHERE ID_STORIA = ?", new ScenarioRowMapper(), storyId);

        } catch (DataAccessException e) {
            logger.error("Lanciata eccezione nel metodo getScenariosByStoryId della classe ScenarioService. Causa dell'" +
                    "eccezione: {}. Descrizione dell'eccezione: {}", e.getCause(), e.getMessage());
            return null;
        }
    }

    public void connectScenarios(int start, int end, int story, String description) {
        String sql = "INSERT INTO COLLEGAMENTI(SCENARIO_PARTENZA, SCENARIO_ARRIVO, STORIA_APPARTENENZA, DESCRIZIONE) VALUES (?, ?, ?, ?)";

        jdbcTemplate.update(sql, start, end, story, description);
    }

    public void setInitialScenario(int storyId, int scenarioId){
        String sql = "UPDATE STORIE SET INITIAL_SCENARIO = ? WHERE ID = ?";
        jdbcTemplate.update(sql, scenarioId, storyId);
    }

    public List<Scenario> getNextScenariosByScenarioId(int scenarioPartenzaId) {
        String sql = "SELECT * FROM SCENARI WHERE ID IN (SELECT SCENARIO_ARRIVO FROM COLLEGAMENTI WHERE SCENARIO_PARTENZA = ? )";
        List<Scenario> nextScenarios = jdbcTemplate.query(sql, new ScenarioRowMapper(), scenarioPartenzaId);
        return nextScenarios;
    }

    public Scenario getScenarioGiusto(int scenarioPartenzaId){
        String sql = "SELECT SCENARIO_ARRIVO FROM COLLEGAMENTI WHERE SCENARIO_PARTENZA = ? AND DESCRIZIONE = 'Risposta giusta'";

        Integer scenarioGiustoId = jdbcTemplate.queryForObject(sql, Integer.class, scenarioPartenzaId);

        Scenario scenarioGiusto = this.getScenarioById(scenarioGiustoId);
        return scenarioGiusto;
    }

    public Scenario getScenarioSbagliato(int scenarioPartenzaId){
        String sql = "SELECT SCENARIO_ARRIVO FROM COLLEGAMENTI WHERE SCENARIO_PARTENZA = ? AND DESCRIZIONE = 'Risposta sbagliata'";

        Integer scenarioSbagliatoId = jdbcTemplate.queryForObject(sql, Integer.class, scenarioPartenzaId);

        Scenario scenarioSbagliato = this.getScenarioById(scenarioSbagliatoId);
        return scenarioSbagliato;
    }


    public String updateDescription(int scenarioId, String newDescription) throws SQLException {
        try {
            String sql = "UPDATE SCENARI SET DESCRIZIONE = ? WHERE ID = ?";
            jdbcTemplate.update(sql, newDescription, scenarioId);

            return newDescription;
        } catch (DataAccessException e) {
            logger.error("Errore nell'eccezione al database nel metodo updateDescription della classe ScenarioService." +
                    "Messaggio: {}, Causa: {}",e.getMessage(), e.getCause());
            return "Errore";
        }
    }

}
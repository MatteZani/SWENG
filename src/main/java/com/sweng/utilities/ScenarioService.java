package com.sweng.utilities;

import com.sweng.entity.Scenario;
import com.sweng.entity.ScenarioBuilder;
import com.sweng.entity.Story;
import com.sweng.mapper.ScenarioRowMapper;
import com.sweng.mapper.StoryRowMapper;
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
public class ScenarioService {

    @Autowired
    private HttpSession httpSession;


    @Autowired
    private StoryService storyService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    Logger logger = LoggerFactory.getLogger(ScenarioService.class);


    public ResponseEntity<Object> createScenario(Scenario scenario) {
        try {
            String sql = "INSERT INTO SCENARI(DESCRIZIONE,ID_STORIA) VALUES (?,?)";
            jdbcTemplate.update(sql, scenario.getDescription(), scenario.getStoryId());
            return new ResponseEntity<>(scenario, HttpStatus.OK);
        } catch (DataAccessException e) {
            logger.error("Lanciata eccezione nel metodo createScenario della classe DBHandler. Causa dell'eccezione: {}. Descrizione dell'eccezione: {}", e.getCause(), e.getMessage());
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
            logger.error("Lanciata eccezione nel metodo createScenario della classe DBHandler. Causa dell'eccezione: {}. Descrizione dell'eccezione: {}", e.getCause(), e.getMessage());
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
            logger.error("NullPointerException nel metodo getMaxScenarioId della classe DBHandler");
            return 1;
        }

    }


    // Metodo per aggiungere uno scenario alla storia nel database
    public void addScenarioToStory(int storyId) {
        try {
            String readScenario = "SELECT INITIAL_SCENARIO FROM STORIE WHERE ID = ?";
            int scenarioId = jdbcTemplate.queryForObject(readScenario, Integer.class);
            if(Integer.valueOf(scenarioId).equals(null)){
                String addScenario = "UPDATE STORIE SET INITIAL_SCENARIO = ? WHERE ID = ?";
                jdbcTemplate.update(addScenario, httpSession.getAttribute("currentScenarioId"), storyId);
            }
        } catch (DataAccessException e) {
            logger.error("Lanciata eccezione nel metodo addScenarioToStory della classe DBHandler. Causa dell'eccezione: {}. Descrizione dell'eccezione: {}", e.getCause(), e.getMessage());
        }
    }


    public List<Map<String, Object>> getScenariosByStoryId(int storyId) {
        try {
            return jdbcTemplate.queryForList("SELECT * FROM SCENARI WHERE ID_STORIA = ?", storyId);

        } catch (DataAccessException e) {
            logger.error("Lanciata eccezione nel metodo getScenariosByStoryId della classe DBHandler. Causa dell'" +
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


}

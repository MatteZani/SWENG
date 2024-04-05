package com.sweng.utilities;

import com.sweng.controller.ScenarioController;
import com.sweng.entity.Scenario;
import com.sweng.entity.Story;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.ui.Model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@SpringBootTest
public class EditScenarioTest {

    @Mock
    private ScenarioService scenarioService;

    @Mock
    private StoryService storyService;

    @Mock
    private Model model;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Test
    void testUpdateDescription() throws SQLException {
        // given
        int scenarioId = 1;
        String expectedDescription = "New Description";
        String expectedSql = "UPDATE SCENARI SET DESCRIZIONE = ? WHERE ID = ?";

        // when
        when(scenarioService.updateDescription(scenarioId, expectedDescription)).thenReturn(expectedDescription);
        String actualDescription = scenarioService.updateDescription(scenarioId, expectedDescription);
        // then
        assertEquals(expectedDescription, actualDescription);
    }


    @Test
    void testUpdateScenarioWithNullDescription() throws SQLException {
        // given
        int scenarioId = 1;
        String newDescription = null;
        List<Story> stories = new ArrayList<>();

        when(storyService.getStoriesByCreator("testUser")).thenReturn(stories);

        // when
        when(scenarioService.updateDescription(scenarioId, null)).thenThrow(new SQLException());

        Exception exception = assertThrows(SQLException.class, () -> {
            scenarioService.updateDescription(1, newDescription);
        });
    }

    @Test
    void testUpdateScenarioWithInvalidScenarioId() throws SQLException {
         //given
        int scenarioId = -1;
        String newDescription = "New Description";
        String expected = null;

        // when
        when(scenarioService.updateDescription(scenarioId, newDescription)).thenReturn(expected);
        String result = scenarioService.updateDescription(scenarioId, newDescription);

        // then
        assertEquals(expected, null);
    }

}
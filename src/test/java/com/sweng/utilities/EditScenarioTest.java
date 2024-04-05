package com.sweng.utilities;

import com.sweng.controller.ScenarioController;
import com.sweng.entity.Story;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    void testUpdateDescription() {
        // given
        int scenarioId = 1;
        String newDescription = "New Description";

        // when
        scenarioService.updateDescription(scenarioId, newDescription);

        // then
        verify(jdbcTemplate).update("UPDATE SCENARI SET DESCRIZIONE = ? WHERE ID = ?", newDescription, scenarioId);
    }

    @Test
    void testUpdateScenario() {
        // given
        int scenarioId = 1;
        String newDescription = "New Description";
        List<Story> stories = new ArrayList<>();

        when(storyService.getStoriesByCreator("testUser")).thenReturn(stories);

        // when
        String result = ScenarioController.updateScenario(scenarioId, newDescription, model);

        // then
        verify(scenarioService).updateDescription(scenarioId, newDescription);
        verify(model).addAttribute("successMessage", "Scenario modificato con successo!");
        verify(model).addAttribute("stories", stories);
        assertEquals("owner-catalog", result);
    }

    @Test
    void testUpdateScenarioWithNullDescription() {
        // given
        int scenarioId = 1;
        String newDescription = null;
        List<Story> stories = new ArrayList<>();

        when(storyService.getStoriesByCreator("testUser")).thenReturn(stories);

        // when
        String result = ScenarioController.updateScenario(scenarioId, newDescription, model);

        // then
        verify(scenarioService).updateDescription(scenarioId, newDescription);
        verify(model).addAttribute("successMessage", "Scenario modificato con successo!");
        verify(model).addAttribute("stories", stories);
        assertEquals("owner-catalog", result);
    }

    @Test
    void testUpdateScenarioWithEmptyDescription() {
        // given
        int scenarioId = 1;
        String newDescription = "";
        List<Story> stories = new ArrayList<>();

        when(storyService.getStoriesByCreator("testUser")).thenReturn(stories);

        // when
        String result = ScenarioController.updateScenario(scenarioId, newDescription, model);

        // then
        verify(scenarioService).updateDescription(scenarioId, newDescription);
        verify(model).addAttribute("successMessage", "Scenario modificato con successo!");
        verify(model).addAttribute("stories", stories);
        assertEquals("owner-catalog", result);
    }

    @Test
    void testUpdateScenarioWithInvalidScenarioId() {
        // given
        int scenarioId = -1;
        String newDescription = "New Description";
        List<Story> stories = new ArrayList<>();

        when(storyService.getStoriesByCreator("testUser")).thenReturn(stories);

        // when
        String result = ScenarioController.updateScenario(scenarioId, newDescription, model);

        // then
        verify(scenarioService).updateDescription(scenarioId, newDescription);
        verify(model).addAttribute("successMessage", "Scenario modificato con successo!");
        verify(model).addAttribute("stories", stories);
        assertEquals("owner-catalog", result);
    }


}
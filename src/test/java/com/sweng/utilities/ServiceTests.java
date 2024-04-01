package com.sweng.utilities;


import com.sweng.entity.Riddle;
import com.sweng.entity.Scenario;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ServiceTests {

    @Mock
    ScenarioService scenarioService;

    @Mock
    ElementService elementService;

    @Test
    void getScenarioByIdTest(){
        //given
        Scenario expectedScenario = new Scenario();
        expectedScenario.setId(1);
        expectedScenario.setStoryId(1);
        expectedScenario.setDescription("Scenario Description");

        //when
        Scenario actualScenario = new Scenario();
        actualScenario.setId(1);
        actualScenario.setStoryId(1);
        actualScenario.setDescription("Scenario Description");
        when(scenarioService.getScenarioById(1)).thenReturn(actualScenario);

        //then
        assertEquals(expectedScenario.getDescription(), scenarioService.getScenarioById(1).getDescription());
        assertEquals(expectedScenario.getId(), scenarioService.getScenarioById(1).getId());
        assertEquals(expectedScenario.getStoryId(), scenarioService.getScenarioById(1).getStoryId());
        assertEquals(expectedScenario.getFoundObjectId(), scenarioService.getScenarioById(1).getFoundObjectId());
    }

    @Test
    void getRiddleByIdTest(){
        //given
        Riddle expectedRiddle = new Riddle();
        expectedRiddle.setId(1);
        expectedRiddle.setQuestion("Domanda dell'indovinello");
        expectedRiddle.setAnswer("Risposta dell'indovinello");

        Riddle actualRiddle = new Riddle();
        actualRiddle.setId(1);
        actualRiddle.setQuestion("Domanda dell'indovinello");
        actualRiddle.setAnswer("Risposta dell'indovinello");

        when(elementService.getRiddleById(1)).thenReturn(actualRiddle);

        assertEquals(expectedRiddle.getId(), elementService.getRiddleById(1).getId());
        assertEquals(expectedRiddle.getAnswer(), elementService.getRiddleById(1).getAnswer());
        assertEquals(expectedRiddle.getQuestion(), elementService.getRiddleById(1).getQuestion());

    }
}

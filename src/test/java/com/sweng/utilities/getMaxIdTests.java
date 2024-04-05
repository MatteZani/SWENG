package com.sweng.utilities;


import com.sweng.entity.Scenario;
import com.sweng.entity.Story;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class getMaxIdTests {
    @Mock
    private ScenarioService scenarioService;

    @Mock
    private StoryService storyService;

    @Test
    void getMaxScenarioIdTest(){
        //given
        int expectedMaxId = 2;

        Scenario firstScenario = new Scenario();
        firstScenario.setId(1);

        Scenario secondScenario = new Scenario();
        secondScenario.setId(expectedMaxId);


        //when
        when(scenarioService.getMaxScenarioId()).thenReturn(Math.max(firstScenario.getId(), secondScenario.getId()));
        int actualMaxId = scenarioService.getMaxScenarioId();

        //then
        assertEquals(expectedMaxId, actualMaxId);

    }

    @Test
    void getMaxStoryId(){
        //given
        int expectedMaxId = 2;

        Story firstStory = new Story();
        firstStory.setId(1);

        Story secondStory = new Story();
        secondStory.setId(expectedMaxId);

        //when
        when(storyService.getMaxStoryId()).thenReturn(Math.max(firstStory.getId(), secondStory.getId()));

        //then
        assertEquals(expectedMaxId, storyService.getMaxStoryId());

    }
}
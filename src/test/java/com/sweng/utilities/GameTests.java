package com.sweng.utilities;

import com.sweng.controller.GameController;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@SpringBootTest
public class GameTests {

    @Mock
    private GameService gameService;
    @InjectMocks
    private GameController gameController;

    @Mock
    private ElementService elementService;

    @Mock
    private StoryService storyService;
    @Mock
    private Model model;
    @Mock
    private JdbcTemplate jdbcTemplate;

    @Test
    void testDeleteGameSession() {
        // given
        int storyId = 1;
        String username = "testUser";

        // when
        when(gameService.deleteGameSession(storyId, username)).thenReturn(storyId);

        // then
        assertEquals(gameService.deleteGameSession(storyId, username), storyId);
    }

    @Test
    void testDeleteGameSessionNotFound() {
        // given
        int storyId = -1;
        String username = "nonExistingUser";
        Integer expected = null;

        // when
        when(gameService.deleteGameSession(storyId, username)).thenReturn(expected);

        // then
        assertNull(expected);
    }

    @Test
    void testDeleteGameSessionWithNullUsername() {
        // given
        int storyId = 1;
        String username = null;
        Integer expected = null;

        // when
        when(gameService.deleteGameSession(storyId, username)).thenReturn(expected);

        // then
        assertNull(expected);
    }
}
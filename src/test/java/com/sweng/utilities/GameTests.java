package com.sweng.utilities;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@SpringBootTest
public class GameTests {

    @Mock
    private GameService gameService;

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
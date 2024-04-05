package com.sweng.utilities;

import com.sweng.controller.GameController;
import com.sweng.entity.GameSession;
import com.sweng.entity.Story;
import com.sweng.entity.User;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

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
        gameService.deleteGameSession(storyId, username);

        // then
        assertEquals(gameService.deleteGameSession(storyId, username));
    }

    @Test
    void testDeleteGameSessionNotFound() {
        // given
        int storyId = 1;
        String username = "nonExistingUser";

        // when
        gameService.deleteGameSession(storyId, username);

        // then
        verify(gameService, never()).deleteGameSession(storyId, username);
        // Assicurati che il metodo deleteGameSession del GameService non venga chiamato
        // se lo username non esiste nel database
    }

    @Test
    void testDeleteGameSessionWithNullUsername() {
        // given
        int storyId = 1;
        String username = null;

        // when
        gameService.deleteGameSession(storyId, username);

        // then
        verify(gameService, never()).deleteGameSession(storyId, username);
        // Assicurati che il metodo deleteGameSession del GameService non venga chiamato
        // se lo username è nullo
    }

    @Test
    void testCancelGame() {
        // given
        int storyId = 1;
        String username = "testUser";

        // when
        gameController.cancelGame(storyId, model);

        // then
        verify(gameService).deleteGameSession(storyId, username);
        verify(elementService).deleteObjectFromInventory(storyId, username);
        verify(storyService).getStories();
        verify(storyService).getSavedIdStories(username);
        // Verifica che i metodi corrispondenti del GameService, ElementService e StoryService vengano chiamati correttamente
    }

    @Test
    void testSaveGameSession_ExistingSession() {
        // given
        GameSession gameSession = new GameSession(new Story("Test Story", "Test Plot", 1,
                "Test Creator", "Horror"), new User("testUser","testPass"),1);
        gameSession.setCurrentScenario(2);

        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq("testUser"), eq(1))).thenReturn(1);

        // when
        gameService.saveGameSession(gameSession);

        // then
        verify(jdbcTemplate).update(anyString(), eq(2), eq("testUser"), eq(1));
    }

    @Test
    void testSaveGameSession_NewSession() {
        // given
        GameSession gameSession = new GameSession(new Story("Test Story", "Test Plot", 1,
                "Test Creator", "Horror"), new User("testUser","testPass"),1);
        gameSession.setCurrentScenario(3);

        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq("testUser"), eq(2))).thenReturn(0);

        // when
        gameService.saveGameSession(gameSession);

        // then
        verify(jdbcTemplate).update(anyString(), eq("testUser"), eq(2), eq(3));
    }

}
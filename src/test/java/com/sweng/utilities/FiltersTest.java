package com.sweng.utilities;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.sweng.entity.Story;
import com.sweng.mapper.StoryRowMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
public class FiltersTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private StoryService storyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testFindStoriesByFilter() {
        // Dati di input
        String title = "Test";
        String category = "Azione";
        String creator = "TestCreator";

        // Query SQL attesa
        String expectedSql = "SELECT * FROM STORIE WHERE 1=1";
        expectedSql += " AND TITLE LIKE ?";
        expectedSql += " AND CATEGORY = ?";
        expectedSql += " AND CREATOR = ?";

        // Risultato atteso della query
        Story firstStory = new Story();
        firstStory.setId(1);

        Story secondStory = new Story();
        secondStory.setId(2);

        List<Story> expectedStories = Arrays.asList(firstStory, secondStory);

        // Mock della risposta della query
        when(jdbcTemplate.query(eq(expectedSql), any(StoryRowMapper.class), eq("%Test%"), eq("Azione"), eq("TestCreator")))
                .thenReturn(expectedStories);

        // Esecuzione del metodo da testare
        List<Story> actualStories = storyService.findStoriesByFilter(title, category, creator);
        // Verifica dell'output
        assertEquals(expectedStories, actualStories);
    }

    @Test
    void testFilter_DataAccessExceptionHandling() {
        // Dati di input
        String title = "Test";
        String category = "Azione";
        String creator = "TestCreator";

        // Mock dell'eccezione di accesso ai dati
        when(jdbcTemplate.query(any(String.class), any(StoryRowMapper.class), any(Object[].class)))
                .thenThrow(new DataAccessException("Simulated data access exception") {});

        // Esecuzione del metodo da testare
        List<Story> actualStories = storyService.findStoriesByFilter(title, category, creator);

        // Verifica che la lista restituita sia vuota a causa dell'eccezione
        assertNotNull(actualStories);
        assertTrue(actualStories.isEmpty());
    }

    @Test
    void testSearchStoriesWithoutFilters() {
        // given
        List<Story> expectedStories = Arrays.asList(
                new Story( "Title 1","Plot 1",1, "Creator 1", "Horror"),
                new Story( "Title 2","Plot 2",2, "Creator 2", "Fantasy")
        );

        when(storyService.findStoriesByFilter(null, null, null)).thenReturn(expectedStories);

        // when
        List<Story> actualStories = storyService.findStoriesByFilter(null, null, null);

        // then
        assertEquals(expectedStories.size(), actualStories.size());
        assertEquals(expectedStories, actualStories);
    }

    @Test
    void testSearchStoriesByTitle() {
        // given
        String title = "Horror Story";
        List<Story> expectedStories = Collections.singletonList(
                new Story("Title 1", "Plot 1", 1, "Creator 1", "Horror")
        );

        when(storyService.findStoriesByFilter(title, null, null)).thenReturn(expectedStories);

        // when
        List<Story> actualStories = storyService.findStoriesByFilter(title, null, null);

        // then
        assertEquals(expectedStories.size(), actualStories.size());
        assertEquals(expectedStories, actualStories);
    }

    @Test
    void testSearchStoriesByCategory() {
        // given
        String category = "Fantasy";
        List<Story> expectedStories = Collections.singletonList(
                new Story("Title 2", "Plot 2", 2, "Creator 2", "Fantasy")
        );

        when(storyService.findStoriesByFilter(null, category, null)).thenReturn(expectedStories);

        // when
        List<Story> actualStories = storyService.findStoriesByFilter(null, category, null);

        // then
        assertEquals(expectedStories.size(), actualStories.size());
        assertEquals(expectedStories, actualStories);
    }

    @Test
    void testSearchStoriesByCreator() {
        // given
        String creator = "Author";
        List<Story> expectedStories = Collections.singletonList(
                new Story("Title 3", "Plot 3", 3, creator, "Comedy")
        );

        when(storyService.findStoriesByFilter(null, null, creator)).thenReturn(expectedStories);

        // when
        List<Story> actualStories = storyService.findStoriesByFilter(null, null, creator);

        // then
        assertEquals(expectedStories.size(), actualStories.size());
        assertEquals(expectedStories, actualStories);
    }

    @Test
    void testSearchStoriesWithNullFilters() {
        // given
        List<Story> expectedStories = Arrays.asList(
                new Story("Title 1", "Plot 1", 1, "Creator 1", "Horror"),
                new Story("Title 2", "Plot 2", 2, "Creator 2", "Fantasy")
        );

        when(storyService.findStoriesByFilter(null, null, null)).thenReturn(expectedStories);

        // when
        List<Story> actualStories = storyService.findStoriesByFilter(null, null, null);

        // then
        assertEquals(expectedStories.size(), actualStories.size());
        assertEquals(expectedStories, actualStories);
    }


}
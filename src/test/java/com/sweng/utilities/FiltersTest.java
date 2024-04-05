package com.sweng.utilities;

import com.sweng.entity.Story;
import com.sweng.mapper.StoryRowMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class FiltersTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
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
        when(storyService.findStoriesByFilter(title, category, creator))
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
    void storiesFilteredByTitleAndCategory() {
        StoryService storyService = mock(StoryService.class);

        // Definizione dei filtri di ricerca
        String titleFilter = "Avventura";
        String categoryFilter = "Fantasy";
        int initialScenarioId = 1;

        // Creazione di una lista di storie attese come risultato
        List<Story> expectedStories = Arrays.asList(
                new Story(1, "Avventura nel bosco incantato", "Il bosco incantato è pieno di misteri", initialScenarioId, "Fantasy", "Alice"),
                new Story(2, "Avventura sulle montagne", "Le montagne nascondono segreti antichi", initialScenarioId, "Fantasy", "Bob")
        );

        // Configurazione del comportamento atteso del servizio di ricerca storie
        when(storyService.findStoriesByFilter(titleFilter, categoryFilter, null))
                .thenReturn(expectedStories);

        // When: Esecuzione dell'azione da testare
        List<Story> actualStories = storyService.findStoriesByFilter(titleFilter, categoryFilter, null);

        // Then: Verifica che il risultato sia quello atteso
        assertFalse(actualStories.isEmpty());

        assertTrue(actualStories.stream()
                .allMatch(story -> story.getTitle().contains(titleFilter) && story.getCategory().equals(categoryFilter)));

        verify(storyService).findStoriesByFilter(titleFilter, categoryFilter, null);
    }

    @Test
    void noStoriesFoundForGivenFilters() {

        StoryService storyService = mock(StoryService.class);
        // Definizione dei filtri di ricerca che non produrranno risultati
        String titleFilter = "TitoloInesistente";
        String categoryFilter = "CategoriaInesistente";

        // Configurazione del mock del StoryService per restituire una lista vuota
        // quando invocato con i filtri che non corrispondono a nessuna storia
        when(storyService.findStoriesByFilter(titleFilter, categoryFilter, null))
                .thenReturn(new ArrayList<>());

        // When: Esecuzione dell'azione da testare
        List<Story> actualStories = storyService.findStoriesByFilter(titleFilter, categoryFilter, null);

        // Then: Verifica dei risultati
        // Asserzione che la lista di storie restituita sia effettivamente vuota
        assertTrue(actualStories.isEmpty());

        // Verifica che il metodo findStoriesByFilter sia stato chiamato con i parametri corretti
        verify(storyService).findStoriesByFilter(titleFilter, categoryFilter, null);
    }


}
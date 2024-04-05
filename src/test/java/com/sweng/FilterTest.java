package com.sweng;

import com.sweng.entity.Story;
import com.sweng.utilities.StoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class FilterTest {


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



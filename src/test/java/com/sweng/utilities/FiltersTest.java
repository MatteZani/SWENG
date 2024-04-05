package com.sweng.utilities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import com.sweng.entity.Story;
import com.sweng.mapper.StoryRowMapper;
import com.sweng.utilities.StoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
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
}

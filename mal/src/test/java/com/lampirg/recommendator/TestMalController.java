package com.lampirg.recommendator;

import com.lampirg.recommendator.anidb.general.SimilarAnimeCommunicator;
import com.lampirg.recommendator.anidb.titles.model.AnimeRecommendation;
import com.lampirg.recommendator.anidb.titles.model.AnimeTitle;
import com.lampirg.recommendator.config.qualifiers.Mal;
import com.lampirg.recommendator.controller.MalController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Set;

@WebMvcTest(MalController.class)
public class TestMalController {
    @MockBean
    @Mal("single")
    private SimilarAnimeCommunicator malCommunicator;
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Test that for user certain titles will be found and sorted")
    void testRecommendationsForUser() throws Exception {
        Set<AnimeRecommendation> recommendations = Set.of(
                new AnimeRecommendation(
                        new AnimeTitle(1, "Hadaske", "/notfound"), 4
                ),
                new AnimeRecommendation(
                        new AnimeTitle(2, "Hadaske: Return to Omsk", "/totally-notfound"), 7
                ),
                new AnimeRecommendation(
                        new AnimeTitle(3, "DeHadaske", "/no"), 1
                ),
                new AnimeRecommendation(
                        new AnimeTitle(4, "Hadaske - Kukic", "/notfound"), 6
                )
        );
        String nickname = "lampirg";
        String json = """
                {
                	"animeRecommendations": [
                		{
                			"title": {
                				"id": 2,
                				"name": "Hadaske: Return to Omsk",
                				"imageUrl": "/totally-notfound"
                			},
                			"numOfRecommendations": 7
                		},
                		{
                			"title": {
                				"id": 4,
                				"name": "Hadaske - Kukic",
                				"imageUrl": "/notfound"
                			},
                			"numOfRecommendations": 6
                		},
                		{
                			"title": {
                				"id": 1,
                				"name": "Hadaske",
                				"imageUrl": "/notfound"
                			},
                			"numOfRecommendations": 4
                		},
                		{
                			"title": {
                				"id": 3,
                				"name": "DeHadaske",
                				"imageUrl": "/no"
                			},
                			"numOfRecommendations": 1
                		}
                    ]
                }
                """;
        Mockito.when(malCommunicator.getSimilarAnimeTitles(nickname)).thenReturn(recommendations);
        mockMvc.perform(MockMvcRequestBuilders.get("/mal/{nickname}", nickname))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(json, true));
    }

    @Test
    @DisplayName("Test controller when searching for recommendations for not existing user")
    void testNotFound() throws Exception {
        String wrongNickname = "lampnirg";
        Mockito.when(malCommunicator.getSimilarAnimeTitles(wrongNickname)).thenThrow(
                HttpClientErrorException.create(HttpStatus.NOT_FOUND, "", HttpHeaders.EMPTY, null, null)
        );
        mockMvc.perform(MockMvcRequestBuilders.get("/mal/{nickname}", wrongNickname))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("user not found"));
    }
}

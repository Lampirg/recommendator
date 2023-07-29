package com.lampirg.recommendator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lampirg.recommendator.anidb.titles.model.AnimeRecommendation;
import com.lampirg.recommendator.anidb.titles.model.AnimeRecommendationList;
import com.lampirg.recommendator.anidb.titles.model.AnimeTitle;
import com.lampirg.recommendator.controller.SimilarAnimeController;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@WebMvcTest(SimilarAnimeController.class)
public class TestSimilarAnimeController {

    @MockBean
    private RestTemplate restTemplate;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    void testController() {
        AnimeRecommendationList returnList = new AnimeRecommendationList(
                List.of(
                        new AnimeRecommendation(
                                new AnimeTitle(1, "Hadaske", "/not-found")
                                , 15)
                )
        );
        Mockito.when(restTemplate.getForEntity(
                "http://mal/mal/user",
                AnimeRecommendationList.class
        )).thenReturn(ResponseEntity.of(Optional.of(returnList)));
        mockMvc.perform(MockMvcRequestBuilders.get("/recommend")
                        .header("service", "mal")
                        .header("username", "user"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(returnList)));
    }
}

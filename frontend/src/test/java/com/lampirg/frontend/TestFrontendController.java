package com.lampirg.frontend;

import com.lampirg.frontend.controller.FrontendController;
import com.lampirg.frontend.service.AnimeRecommendationService;
import com.lampirg.frontend.service.RecommendatorCommunicator;
import com.lampirg.recommendator.anidb.titles.model.AnimeRecommendation;
import com.lampirg.recommendator.anidb.titles.model.AnimeRecommendationList;
import com.lampirg.recommendator.anidb.titles.model.AnimeTitle;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebMvcTest(FrontendController.class)
public class TestFrontendController {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AnimeRecommendationService communicator;


    @Test
    @SneakyThrows
    void testGetPage() {
        mockMvc.perform(MockMvcRequestBuilders.get("/recommend"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_HTML));
    }

    @Test
    @SneakyThrows
    void testPostPage() {
        FrontendController.Request request = new FrontendController.Request();
        request.setService("mal");
        request.setUsername("user");
        var attributes = Map.of(
                "service", List.of(request.getService()),
                "username", List.of(request.getUsername())
        );
        HttpHeaders headers = new HttpHeaders();
        headers.putAll(attributes);
        AnimeRecommendationList returnList = new AnimeRecommendationList(
                List.of(
                        new AnimeRecommendation(
                                new AnimeTitle(1, "Hadaske", "/not-found")
                                , 15)
                )
        );
        Mockito.when(communicator.getAnimeRecommendationList(headers)).thenReturn(returnList);
        mockMvc.perform(MockMvcRequestBuilders.post("/recommend").flashAttr("request", request))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_HTML));
    }

}

package com.lampirg.recommendator.unit;

import com.lampirg.recommendator.anidb.MalRecommendationsFinder;
import com.lampirg.recommendator.anidb.MalQueryMaker;
import com.lampirg.recommendator.anidb.json.MainPicture;
import com.lampirg.recommendator.anidb.json.MalNode;
import com.lampirg.recommendator.anidb.json.Recommendation;
import com.lampirg.recommendator.anidb.json.queries.GetAnimeDetail;
import com.lampirg.recommendator.anidb.titles.model.AnimeTitle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class TestMalRecommendationsFinder {
    @Mock
    private MalQueryMaker malQueryMaker;
    @InjectMocks
    private MalRecommendationsFinder malRecommendationsFinder;

    private final GetAnimeDetail json = new GetAnimeDetail(
            List.of(
                    new Recommendation(
                            new MalNode(1, "Hadaske", new MainPicture(null, "/notfound")), 4
                    ),
                    new Recommendation(
                            new MalNode(2, "Hadaske: Return to Omsk", new MainPicture("/totally-notfound-large", "/totally-notfound")), 6
                    )
            )
    );

    private final List<AnimeTitle> titles = List.of(
            new AnimeTitle(1, "Hadaske", "/notfound"),
            new AnimeTitle(2, "Hadaske: Return to Omsk", "/totally-notfound"),
            new AnimeTitle(3, "DeHadaske", "/no"),
            new AnimeTitle(4, "Hadaske - Kukic", "/notfound")
    );

    @Test
    @DisplayName("Test MalCacher class")
    void testQuery() {
        Mockito.when(malQueryMaker.exchange(
                "https://api.myanimelist.net/v2/anime/3?fields=recommendations",
                HttpMethod.GET,
                GetAnimeDetail.class
        )).thenReturn(ResponseEntity.of(Optional.of(json)));
        Assertions.assertEquals(
                Set.of(
                        new AnimeTitle(
                                json.recommendations().get(0).node().id(),
                                json.recommendations().get(0).node().title(),
                                json.recommendations().get(0).node().mainPicture().getLargeIfPresent()
                        ),
                        new AnimeTitle(
                                json.recommendations().get(1).node().id(),
                                json.recommendations().get(1).node().title(),
                                json.recommendations().get(1).node().mainPicture().getLargeIfPresent())),
                malRecommendationsFinder.findRecommendations(titles.get(2))
        );
    }
}

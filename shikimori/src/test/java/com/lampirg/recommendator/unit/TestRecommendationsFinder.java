package com.lampirg.recommendator.unit;

import com.lampirg.recommendator.anidb.query.ShikimoriRecommendationsFinder;
import com.lampirg.recommendator.anidb.query.ShikimoriQueryMaker;
import com.lampirg.recommendator.anidb.json.Image;
import com.lampirg.recommendator.anidb.json.ShikiNode;
import com.lampirg.recommendator.anidb.titles.model.AnimeTitle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class TestRecommendationsFinder {
    @Mock
    private ShikimoriQueryMaker shikimoriQueryMaker;
    @InjectMocks
    private ShikimoriRecommendationsFinder shikimoriRecommendationsFinder;

    private final List<ShikiNode> json = List.of(
            new ShikiNode(1, "Hadaske", new Image("/notfound")),
            new ShikiNode(2, "Hadaske: Return to Omsk", new Image("/totally-notfound"))
    );

    private final List<AnimeTitle> titles = List.of(
            new AnimeTitle(1, "Hadaske", "/notfound"),
            new AnimeTitle(2, "Hadaske: Return to Omsk", "/totally-notfound"),
            new AnimeTitle(3, "DeHadaske", "/no"),
            new AnimeTitle(4, "Hadaske - Kukic", "/notfound")
    );

    @Test
    @DisplayName("Test data extractor (ShikimoriCacher)")
    void testQuery() {
        Mockito.when(shikimoriQueryMaker.exchange(
                "https://shikimori.one/api/animes/3/similar",
                HttpMethod.GET,
                new ParameterizedTypeReference<List<ShikiNode>>() {
                }
        )).thenReturn(ResponseEntity.of(Optional.of(json)));
        Assertions.assertEquals(
                Set.of(
                        new AnimeTitle(
                                json.get(0).id(),
                                json.get(0).name(),
                                "https://shikimori.one" + json.get(0).image().original()
                        ),
                        new AnimeTitle(
                                json.get(1).id(),
                                json.get(1).name(),
                                "https://shikimori.one" + json.get(1).image().original()
                        )
                ),
                shikimoriRecommendationsFinder.findRecommendations(titles.get(2))
        );
    }
}

package com.lampirg.recommendator.json;

import com.lampirg.recommendator.anidb.DummySpringBootApplication;
import com.lampirg.recommendator.anidb.titles.model.AnimeRecommendation;
import com.lampirg.recommendator.anidb.titles.model.AnimeRecommendationList;
import com.lampirg.recommendator.anidb.titles.model.AnimeTitle;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;
import java.util.List;

@JsonTest
@ContextConfiguration(classes = DummySpringBootApplication.class)
public class TestAnimeRecommendationListJson {
    @Autowired
    private JacksonTester<AnimeRecommendationList> jacksonTester;

    @Test
    void testSerialization() throws IOException {
        AnimeRecommendationList list = new AnimeRecommendationList(List.of(
                new AnimeRecommendation(
                        new AnimeTitle(2, "Hadaske: Return to Omsk", "/totally-notfound"), 7
                ),
                new AnimeRecommendation(
                        new AnimeTitle(4, "Hadaske - Kukic", "/notfound"), 6
                ),
                new AnimeRecommendation(
                        new AnimeTitle(1, "Hadaske", "/notfound"), 4
                ),
                new AnimeRecommendation(
                        new AnimeTitle(3, "DeHadaske", "/no"), 1
                )
        ));
        JsonContent<AnimeRecommendationList> json = jacksonTester.write(list);
        Assertions.assertThat(json).isEqualToJson(new ClassPathResource("response.json"));
    }
}

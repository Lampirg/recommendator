package com.lampirg.recommendator.json;

import com.lampirg.recommendator.anidb.json.MainPicture;
import com.lampirg.recommendator.anidb.json.MalNode;
import com.lampirg.recommendator.anidb.json.Recommendation;
import com.lampirg.recommendator.anidb.json.queries.GetAnimeDetail;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.List;

@JsonTest
public class TestGetAnimeDetail {
    @Autowired
    private JacksonTester<GetAnimeDetail> jacksonTester;

    @Test
    @DisplayName("Test that recommendation list is deserialized correctly")
    void testDeserialization() throws IOException {
        GetAnimeDetail expected = new GetAnimeDetail(List.of(
                new Recommendation(
                        new MalNode(4975, "ChäoS;HEAd", new MainPicture("https://cdn.myanimelist.net/images/anime/13/15443l.jpg", "https://cdn.myanimelist.net/images/anime/13/15443.jpg")),
                        7
                ),
                new Recommendation(
                        new MalNode(355, "Shakugan no Shana", new MainPicture("https://cdn.myanimelist.net/images/anime/8/21197l.jpg", "https://cdn.myanimelist.net/images/anime/8/21197.jpg")),
                        6
                ),
                new Recommendation(
                        new MalNode(10620, "Mirai Nikki (TV)", new MainPicture("https://cdn.myanimelist.net/images/anime/13/33465l.jpg", "https://cdn.myanimelist.net/images/anime/13/33465.jpg")),
                        5
                )
        ));
        GetAnimeDetail actual = jacksonTester.readObject(new ClassPathResource("get anime recommendation/response.json"));
        Assertions.assertEquals(expected, actual);
    }
}

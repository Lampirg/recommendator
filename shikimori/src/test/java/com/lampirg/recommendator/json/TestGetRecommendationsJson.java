package com.lampirg.recommendator.json;

import com.lampirg.recommendator.anidb.json.Image;
import com.lampirg.recommendator.anidb.json.ShikiNode;
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
public class TestGetRecommendationsJson {
    @Autowired
    private JacksonTester<List<ShikiNode>> jacksonTester;

    @Test
    @DisplayName("Test that recommendation list deserialized correctly")
    void testDeserialization() throws IOException {
        List<ShikiNode> expected = List.of(
                new ShikiNode(205, "Samurai Champloo", new Image("/system/animes/original/205.jpg?1674332481")),
                new ShikiNode(6, "Trigun", new Image("/system/animes/original/6.jpg?1674560642"))
        );
        List<ShikiNode> actual = jacksonTester.readObject(new ClassPathResource("get recommendations/response.json"));
        Assertions.assertEquals(expected, actual);
    }
}

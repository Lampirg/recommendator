package com.lampirg.recommendator.json;

import com.lampirg.recommendator.anidb.json.Image;
import com.lampirg.recommendator.anidb.json.ShikiNode;
import com.lampirg.recommendator.anidb.json.ShikiUserNode;
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
public class TestGetUserListJson {
    @Autowired
    private JacksonTester<List<ShikiUserNode>> jacksonTester;

    @Test
    @DisplayName("Test deserialization")
    void testDeserialization() throws IOException {
        List<ShikiUserNode> expected = List.of(
                new ShikiUserNode(10, new ShikiNode(
                        1, "Cowboy Bebop", new Image("/system/animes/original/1.jpg?1674378220")
                )),
                new ShikiUserNode(10, new ShikiNode(
                        30, "Neon Genesis Evangelion", new Image("/system/animes/original/30.jpg?1674378478")
                )
        ));
        List<ShikiUserNode> actual = jacksonTester.readObject(new ClassPathResource("get user list/response.json"));
        Assertions.assertEquals(expected, actual);
    }
}

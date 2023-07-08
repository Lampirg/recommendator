package com.lampirg.recommendator.json;

import com.lampirg.recommendator.anidb.json.Data;
import com.lampirg.recommendator.anidb.json.ListStatus;
import com.lampirg.recommendator.anidb.json.MainPicture;
import com.lampirg.recommendator.anidb.json.MalNode;
import com.lampirg.recommendator.anidb.json.queries.GetUserListJsonResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@JsonTest
public class TestGetUserListJson {
    @Autowired
    JacksonTester<GetUserListJsonResult> jacksonTester;

    @Test
    @DisplayName("Test deserialization of MAL api response if list size is not paged")
    void testDeserializationGivenNoPaging() throws IOException {
        GetUserListJsonResult expected = new GetUserListJsonResult(
                List.of(
                        new Data(
                                new MalNode(53917, "1000-mankai Hug Nanda", new MainPicture("https://cdn.myanimelist.net/images/anime/1793/131968l.jpg", "https://cdn.myanimelist.net/images/anime/1793/131968.jpg")),
                                new ListStatus(0)
                        ),
                        new Data(
                                new MalNode(6682, "11eyes", new MainPicture("https://cdn.myanimelist.net/images/anime/6/73520l.jpg", "https://cdn.myanimelist.net/images/anime/6/73520.jpg")),
                                new ListStatus(4)
                        )
                ),
                Map.of()
        );
        GetUserListJsonResult result = jacksonTester.readObject(new ClassPathResource("get user list/without paging.json"));
        Assertions.assertEquals(expected, result);
    }

    @Test
    @DisplayName("Test deserialization of MAL api response if list size is paged")
    void testDeserialization() throws IOException {
        GetUserListJsonResult expected = new GetUserListJsonResult(
                List.of(
                        new Data(
                                new MalNode(53917, "1000-mankai Hug Nanda", new MainPicture("https://cdn.myanimelist.net/images/anime/1793/131968l.jpg", "https://cdn.myanimelist.net/images/anime/1793/131968.jpg")),
                                new ListStatus(0)
                        ),
                        new Data(
                                new MalNode(6682, "11eyes", new MainPicture("https://cdn.myanimelist.net/images/anime/6/73520l.jpg", "https://cdn.myanimelist.net/images/anime/6/73520.jpg")),
                                new ListStatus(4)
                        )
                ),
                Map.of("next", "https://link-to-next-profile")
        );
        GetUserListJsonResult result = jacksonTester.readObject(new ClassPathResource("get user list/with paging.json"));
        Assertions.assertEquals(expected, result);
    }
}

package com.lampirg.recommendator.json;

import com.lampirg.recommendator.anidb.json.Completed;
import com.lampirg.recommendator.anidb.json.GetUserListAndRecommendations;
import com.lampirg.recommendator.anidb.json.Other;
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
public class TestQuery {
    @Autowired
    private JacksonTester<GetUserListAndRecommendations> jacksonTester;

    @Test
    @DisplayName("Test that query to anilist API is deserialized correctly")
    void testDeserialization() throws IOException {
        List<Completed.CompletedList.Entries> completed = List.of(
                new Completed.CompletedList.Entries(
                        new Completed.CompletedList.Entries.Media(
                                6682,
                                new Completed.CompletedList.Entries.Media.Title("11eyes"),
                                new Completed.CompletedList.Entries.Media.CoverImage(
                                        "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx6682-ZptgLsCCNHjL.jpg",
                                        "https://s4.anilist.co/file/anilistcdn/media/anime/cover/medium/bx6682-ZptgLsCCNHjL.jpg",
                                        "https://s4.anilist.co/file/anilistcdn/media/anime/cover/small/bx6682-ZptgLsCCNHjL.jpg"),
                                new Completed.CompletedList.Entries.Media.Recommendations(List.of(
                                        new Completed.CompletedList.Entries.Media.Recommendations.Nodes(
                                                7,
                                                new Completed.CompletedList.Entries.Media.Recommendations.Nodes.MediaRecommendation(
                                                        10620,
                                                        new Completed.CompletedList.Entries.Media.Title("Mirai Nikki"),
                                                        new Completed.CompletedList.Entries.Media.CoverImage(
                                                                "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx10620-OMnOMuZBgEKy.png",
                                                                "https://s4.anilist.co/file/anilistcdn/media/anime/cover/medium/bx10620-OMnOMuZBgEKy.png",
                                                                "https://s4.anilist.co/file/anilistcdn/media/anime/cover/small/bx10620-OMnOMuZBgEKy.png"
                                                        )
                                                )
                                        ),
                                        new Completed.CompletedList.Entries.Media.Recommendations.Nodes(
                                                4,
                                                new Completed.CompletedList.Entries.Media.Recommendations.Nodes.MediaRecommendation(
                                                        9331,
                                                        new Completed.CompletedList.Entries.Media.Title("Yumekui Merry"),
                                                        new Completed.CompletedList.Entries.Media.CoverImage(
                                                                "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx9331-f2bAShenxOeX.jpg",
                                                                "https://s4.anilist.co/file/anilistcdn/media/anime/cover/medium/bx9331-f2bAShenxOeX.jpg",
                                                                "https://s4.anilist.co/file/anilistcdn/media/anime/cover/small/bx9331-f2bAShenxOeX.jpg"
                                                        )
                                                )
                                        )
                                ))
                        ),
                        4
                )
        );
        List<Other.Lists.Entries> watching = List.of(
                new Other.Lists.Entries(
                        new Other.Lists.Entries.Media(1372,
                                new Other.Lists.Entries.Media.Title("Taiho Shichauzo (TV)"),
                                new Other.Lists.Entries.Media.CoverImage(
                                        "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx1372-Q1B078xXbu2c.png",
                                        "https://s4.anilist.co/file/anilistcdn/media/anime/cover/medium/bx1372-Q1B078xXbu2c.png",
                                        "https://s4.anilist.co/file/anilistcdn/media/anime/cover/small/bx1372-Q1B078xXbu2c.png"
                                )
                        ),
                        0
                )
        );
        List<Other.Lists.Entries> dropped = List.of(
                new Other.Lists.Entries(
                        new Other.Lists.Entries.Media(269,
                                new Other.Lists.Entries.Media.Title("BLEACH"),
                                new Other.Lists.Entries.Media.CoverImage(
                                        "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx269-KxkqTIuQgJ6v.png",
                                        "https://s4.anilist.co/file/anilistcdn/media/anime/cover/medium/bx269-KxkqTIuQgJ6v.png",
                                        "https://s4.anilist.co/file/anilistcdn/media/anime/cover/small/bx269-KxkqTIuQgJ6v.png"
                                )
                        ),
                        0
                )
        );
        List<Other.Lists.Entries> paused = List.of(
                new Other.Lists.Entries(
                        new Other.Lists.Entries.Media(100673,
                                new Other.Lists.Entries.Media.Title("Hisone to Maso-tan"),
                                new Other.Lists.Entries.Media.CoverImage(
                                        "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/nx100673-AXPJdjIXRRED.jpg",
                                        "https://s4.anilist.co/file/anilistcdn/media/anime/cover/medium/nx100673-AXPJdjIXRRED.jpg",
                                        "https://s4.anilist.co/file/anilistcdn/media/anime/cover/small/nx100673-AXPJdjIXRRED.jpg"

                                )
                        ),
                        0
                )
        );
        GetUserListAndRecommendations expected = new GetUserListAndRecommendations(
                new GetUserListAndRecommendations.Data(
                        new Completed(List.of(new Completed.CompletedList("COMPLETED", completed))),
                        new Other(List.of(
                                new Other.Lists("CURRENT", watching),
                                new Other.Lists("DROPPED", dropped),
                                new Other.Lists("PAUSED", paused)
                        ))
                )
        );
        GetUserListAndRecommendations actual = jacksonTester.readObject(new ClassPathResource("anilist api query/response.json"));
        Assertions.assertEquals(expected, actual);
    }
}

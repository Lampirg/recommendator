package com.lampirg.recommendator.unit;

import com.lampirg.recommendator.anidb.json.Data;
import com.lampirg.recommendator.anidb.json.ListStatus;
import com.lampirg.recommendator.anidb.json.MainPicture;
import com.lampirg.recommendator.anidb.json.MalNode;
import com.lampirg.recommendator.anidb.json.queries.GetUserListJsonResult;
import com.lampirg.recommendator.anidb.query.MalQueryMaker;
import com.lampirg.recommendator.anidb.query.MalUserListFinder;
import com.lampirg.recommendator.anidb.titles.model.AnimeTitle;
import com.lampirg.recommendator.anidb.titles.model.UserAnimeTitle;
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

import java.util.*;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
public class TestMalUserListFinder {
    @Mock
    private MalQueryMaker malQueryMaker;
    @InjectMocks
    private MalUserListFinder malUserListFinder;

    private final GetUserListJsonResult jsonWithoutNextPage = new GetUserListJsonResult(
            List.of(
                    new Data(
                            new MalNode(1, "Hadaske", new MainPicture(null, "/notfound")), new ListStatus(10)
                    ),
                    new Data(
                            new MalNode(2, "Hadaske: Return to Omsk", new MainPicture("/totally-notfound-large", "/totally-notfound")), new ListStatus(10)
                    )
            ),
            Map.of()
    );

    private final GetUserListJsonResult jsonWithNextPage = new GetUserListJsonResult(
            List.of(
                    new Data(
                            new MalNode(1, "Hadaske", new MainPicture(null, "/notfound")), new ListStatus(10)
                    ),
                    new Data(
                            new MalNode(2, "Hadaske: Return to Omsk", new MainPicture("/totally-notfound-large", "/totally-notfound")), new ListStatus(10)
                    )
            ),
            Map.of("next", "https://link-to-next-profile")
    );
    private final GetUserListJsonResult nextPage = new GetUserListJsonResult(
            List.of(
                    new Data(
                            new MalNode(3, "DeHadaske", new MainPicture("/gfg", "/ccv")), new ListStatus(4)
                    ),
                    new Data(
                            new MalNode(4, "Hadaske - Kukic", new MainPicture("/fff", "/sss")), new ListStatus(7)
                    )
            ),
            Map.of()
    );

    @Test
    @DisplayName("Test when response has single page")
    void givenNoPaging() {
        Mockito.when(malQueryMaker.exchange(
                "https://api.myanimelist.net/v2/users/lampirg/animelist?fields=list_status&status=completed&limit=1000",
                HttpMethod.GET,
                GetUserListJsonResult.class
        )).thenReturn(ResponseEntity.of(Optional.of(jsonWithoutNextPage)));
        Assertions.assertEquals(
                new ArrayList<>(jsonWithoutNextPage.data()),
                malUserListFinder.findUserList("lampirg", "completed")
        );
    }

    @Test
    @DisplayName("Test when response has multiple pages")
    void givenPaging() {
        Mockito.when(malQueryMaker.exchange(
                "https://api.myanimelist.net/v2/users/lampirg/animelist?fields=list_status&status=completed&limit=1000",
                HttpMethod.GET,
                GetUserListJsonResult.class
        )).thenReturn(ResponseEntity.of(Optional.of(jsonWithNextPage)));
        Mockito.when(malQueryMaker.exchange(
                "https://link-to-next-profile",
                HttpMethod.GET,
                GetUserListJsonResult.class
        )).thenReturn(ResponseEntity.of(Optional.of(nextPage)));
        Assertions.assertEquals(
                Stream.of(jsonWithNextPage.data(), nextPage.data())
                        .flatMap(Collection::stream)
                        .toList(),
                malUserListFinder.findUserList("lampirg", "completed")
        );
    }
}

package com.lampirg.recommendator;

import com.lampirg.recommendator.anidb.MalQueryMaker;
import com.lampirg.recommendator.anidb.general.listextractor.UserListExtractor;
import com.lampirg.recommendator.anidb.json.Data;
import com.lampirg.recommendator.anidb.json.ListStatus;
import com.lampirg.recommendator.anidb.json.MainPicture;
import com.lampirg.recommendator.anidb.json.MalNode;
import com.lampirg.recommendator.anidb.json.queries.GetUserListJsonResult;
import com.lampirg.recommendator.anidb.listextractor.ConcurrentUserListExtractor;
import com.lampirg.recommendator.anidb.listextractor.SingleThreadUserListExtractor;
import com.lampirg.recommendator.anidb.titles.model.AnimeTitle;
import com.lampirg.recommendator.anidb.titles.model.UserAnimeTitle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TestListExtractor {

    @Mock
    MalQueryMaker queryMaker;
    @InjectMocks
    SingleThreadUserListExtractor singleThreadUserListExtractor;
    @InjectMocks
    ConcurrentUserListExtractor concurrentUserListExtractor;

    private final GetUserListJsonResult completedJson = new GetUserListJsonResult(
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

    private final GetUserListJsonResult watchingJson = new GetUserListJsonResult(
            List.of(),
            Map.of()
    );

    private final GetUserListJsonResult onHoldJson = new GetUserListJsonResult(
            List.of(),
            Map.of()
    );

    private final GetUserListJsonResult droppedJson = new GetUserListJsonResult(
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
    void testSingleThread() {
        testListExtractor(singleThreadUserListExtractor);
    }

    @Test
    void testMultiThread() {
        testListExtractor(concurrentUserListExtractor);
    }

    private void testListExtractor(UserListExtractor extractor) {
        when(queryMaker.exchange(Mockito.contains("completed"), Mockito.eq(HttpMethod.GET), Mockito.eq(GetUserListJsonResult.class))).thenReturn(ResponseEntity.of(Optional.of(completedJson)));
        when(queryMaker.exchange(Mockito.contains("dropped"), Mockito.eq(HttpMethod.GET), Mockito.eq(GetUserListJsonResult.class))).thenReturn(ResponseEntity.of(Optional.of(droppedJson)));
        when(queryMaker.exchange(Mockito.contains("watching"), Mockito.eq(HttpMethod.GET), Mockito.eq(GetUserListJsonResult.class))).thenReturn(ResponseEntity.of(Optional.of(watchingJson)));
        when(queryMaker.exchange(Mockito.contains("on_hold"), Mockito.eq(HttpMethod.GET), Mockito.eq(GetUserListJsonResult.class))).thenReturn(ResponseEntity.of(Optional.of(onHoldJson)));
        extractor.setUser("lampirg");
        Assertions.assertEquals(
                completedJson.data()
                        .stream()
                        .map(
                                data -> new UserAnimeTitle(new AnimeTitle(data.node().id(), data.node().title(), data.node().mainPicture().getLargeIfPresent()), data.listStatus().score())
                        )
                        .collect(Collectors.toSet()),
                extractor.getToInclude()
        );
        Assertions.assertEquals(
                Stream.of(droppedJson.data(), completedJson.data(), watchingJson.data(), onHoldJson.data())
                        .flatMap(Collection::stream)
                        .map(
                                data -> new UserAnimeTitle(new AnimeTitle(data.node().id(), data.node().title(), data.node().mainPicture().getLargeIfPresent()), data.listStatus().score())
                        )
                        .collect(Collectors.toSet()),
                extractor.getToExclude()
        );
    }
}

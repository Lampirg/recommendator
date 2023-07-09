package com.lampirg.recommendator.unit;

import com.lampirg.recommendator.anidb.general.listextractor.UserListExtractor;
import com.lampirg.recommendator.anidb.json.Data;
import com.lampirg.recommendator.anidb.json.ListStatus;
import com.lampirg.recommendator.anidb.json.MainPicture;
import com.lampirg.recommendator.anidb.json.MalNode;
import com.lampirg.recommendator.anidb.listextractor.ConcurrentUserListExtractor;
import com.lampirg.recommendator.anidb.listextractor.SingleThreadUserListExtractor;
import com.lampirg.recommendator.anidb.query.MalUserListFinder;
import com.lampirg.recommendator.anidb.titles.model.AnimeTitle;
import com.lampirg.recommendator.anidb.titles.model.UserAnimeTitle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TestListExtractor {

    @Mock
    MalUserListFinder malUserListFinder;
    @InjectMocks
    SingleThreadUserListExtractor singleThreadUserListExtractor;
    @InjectMocks
    ConcurrentUserListExtractor concurrentUserListExtractor;

    private final List<Data> completed = List.of(
                    new Data(
                            new MalNode(1, "Hadaske", new MainPicture(null, "/notfound")), new ListStatus(10)
                    ),
                    new Data(
                            new MalNode(2, "Hadaske: Return to Omsk", new MainPicture("/totally-notfound-large", "/totally-notfound")), new ListStatus(10)
                    )
            );

    private final List<Data> watching = List.of();

    private final List<Data> onHold = List.of();

    private final List<Data> dropped = List.of(
                    new Data(
                            new MalNode(3, "DeHadaske", new MainPicture("/gfg", "/ccv")), new ListStatus(4)
                    ),
                    new Data(
                            new MalNode(4, "Hadaske - Kukic", new MainPicture("/fff", "/sss")), new ListStatus(7)
                    )
            );

    @Test
    @DisplayName("Test SingleThreadUserListExtractor class")
    void testSingleThread() {
        testListExtractor(singleThreadUserListExtractor);
    }

    @Test
    @DisplayName("Test ConcurrentUserListExtractor class")
    void testMultiThread() {
        testListExtractor(concurrentUserListExtractor);
    }

    private void testListExtractor(UserListExtractor extractor) {
        when(malUserListFinder.findUserList("lampirg", "completed")).thenReturn(completed);
        when(malUserListFinder.findUserList("lampirg", "dropped")).thenReturn(dropped);
        when(malUserListFinder.findUserList("lampirg", "watching")).thenReturn(watching);
        when(malUserListFinder.findUserList("lampirg", "on_hold")).thenReturn(onHold);
        extractor.setUser("lampirg");
        Assertions.assertEquals(
                completed
                        .stream()
                        .map(
                                data -> new UserAnimeTitle(new AnimeTitle(data.node().id(), data.node().title(), data.node().mainPicture().getLargeIfPresent()), data.listStatus().score())
                        )
                        .collect(Collectors.toSet()),
                extractor.getToInclude()
        );
        Assertions.assertEquals(
                Stream.of(dropped, completed, watching, onHold)
                        .flatMap(Collection::stream)
                        .map(
                                data -> new UserAnimeTitle(new AnimeTitle(data.node().id(), data.node().title(), data.node().mainPicture().getLargeIfPresent()), data.listStatus().score())
                        )
                        .collect(Collectors.toSet()),
                extractor.getToExclude()
        );
    }
}

package com.lampirg.recommendator.unit;

import com.lampirg.recommendator.anidb.ShikimoriListExtractor;
import com.lampirg.recommendator.anidb.json.Image;
import com.lampirg.recommendator.anidb.json.ShikiNode;
import com.lampirg.recommendator.anidb.json.ShikiUserNode;
import com.lampirg.recommendator.anidb.query.ShikimoriUserListFinder;
import com.lampirg.recommendator.anidb.titles.model.AnimeTitle;
import com.lampirg.recommendator.anidb.titles.model.UserAnimeTitle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TestListExtractor {

    @Mock
    ShikimoriUserListFinder shikimoriUserListFinder;
    @InjectMocks
    ShikimoriListExtractor shikimoriListExtractor;

    private final List<ShikiUserNode> completed = List.of(
            new ShikiUserNode(10, new ShikiNode(1, "Hadaske", new Image("/notfound"))),
            new ShikiUserNode(10, new ShikiNode(2, "Hadaske: Return to Omsk", new Image("/totally-notfound")))
    );

    private final List<ShikiUserNode> watching = List.of();

    private final List<ShikiUserNode> onHold = List.of();

    private final List<ShikiUserNode> dropped = List.of(
            new ShikiUserNode(10, new ShikiNode(3, "DeHadaske", new Image("/ccv"))),
            new ShikiUserNode(10, new ShikiNode(4, "Hadaske - Kukic", new Image("/sss")))
    );

    @Test
    @DisplayName("Test List Extractor")
    void testListExtractor() {
        String username = "lampirg";
        when(shikimoriUserListFinder.findUserList(username, "completed")).thenReturn(completed);
        when(shikimoriUserListFinder.findUserList(username, "dropped")).thenReturn(dropped);
        when(shikimoriUserListFinder.findUserList(username, "watching")).thenReturn(watching);
        when(shikimoriUserListFinder.findUserList(username, "on_hold")).thenReturn(onHold);
        Assertions.assertEquals(
                completed
                        .stream()
                        .map(
                                data -> new UserAnimeTitle(new AnimeTitle(data.anime().id(), data.anime().name(), "https://shikimori.one" + data.anime().image().original()), data.score())
                        )
                        .collect(Collectors.toSet()),
                shikimoriListExtractor.getToInclude(username)
        );
        Assertions.assertEquals(
                Stream.of(dropped, completed, watching, onHold)
                        .flatMap(Collection::stream)
                        .map(
                                data -> new UserAnimeTitle(new AnimeTitle(data.anime().id(), data.anime().name(), "https://shikimori.one" + data.anime().image().original()), data.score())
                        )
                        .collect(Collectors.toSet()),
                shikimoriListExtractor.getToExclude(username)
        );
    }
}

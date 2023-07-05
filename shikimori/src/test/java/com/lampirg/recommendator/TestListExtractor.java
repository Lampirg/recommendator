package com.lampirg.recommendator;

import com.lampirg.recommendator.anidb.ShikimoriListExtractor;
import com.lampirg.recommendator.anidb.ShikimoriQueryMaker;
import com.lampirg.recommendator.anidb.general.listextractor.UserListExtractor;
import com.lampirg.recommendator.anidb.json.Image;
import com.lampirg.recommendator.anidb.json.ShikiNode;
import com.lampirg.recommendator.anidb.json.ShikiUserNode;
import com.lampirg.recommendator.anidb.titles.model.AnimeTitle;
import com.lampirg.recommendator.anidb.titles.model.UserAnimeTitle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TestListExtractor {

    @Mock
    ShikimoriQueryMaker queryMaker;
    @InjectMocks
    ShikimoriListExtractor shikimoriListExtractor;

    private final List<ShikiUserNode> completedJson = List.of(
            new ShikiUserNode(10, "", new ShikiNode(1, "Hadaske", new Image("/notfound"))),
            new ShikiUserNode(10, "", new ShikiNode(2, "Hadaske: Return to Omsk", new Image("/totally-notfound")))
    );

    private final List<ShikiUserNode> watchingJson = List.of();

    private final List<ShikiUserNode> onHoldJson = List.of();

    private final List<ShikiUserNode> droppedJson = List.of(
            new ShikiUserNode(10, "", new ShikiNode(3, "DeHadaske", new Image("/ccv"))),
            new ShikiUserNode(10, "", new ShikiNode(4, "Hadaske - Kukic", new Image("/sss")))
    );

    @Test
    void testSingleThread() {
        when(queryMaker.exchange(Mockito.contains("completed"), Mockito.eq(HttpMethod.GET), Mockito.eq(new ParameterizedTypeReference<List<ShikiUserNode>>() {
        }))).thenReturn(ResponseEntity.of(Optional.of(completedJson)));
        when(queryMaker.exchange(Mockito.contains("dropped"), Mockito.eq(HttpMethod.GET), Mockito.eq(new ParameterizedTypeReference<List<ShikiUserNode>>() {
        }))).thenReturn(ResponseEntity.of(Optional.of(droppedJson)));
        when(queryMaker.exchange(Mockito.contains("watching"), Mockito.eq(HttpMethod.GET), Mockito.eq(new ParameterizedTypeReference<List<ShikiUserNode>>() {
        }))).thenReturn(ResponseEntity.of(Optional.of(watchingJson)));
        when(queryMaker.exchange(Mockito.contains("on_hold"), Mockito.eq(HttpMethod.GET), Mockito.eq(new ParameterizedTypeReference<List<ShikiUserNode>>() {
        }))).thenReturn(ResponseEntity.of(Optional.of(onHoldJson)));
        shikimoriListExtractor.setUser("lampirg");
        Assertions.assertEquals(
                completedJson
                        .stream()
                        .map(
                                data -> new UserAnimeTitle(new AnimeTitle(data.anime().id(), data.anime().name(), "https://shikimori.one" + data.anime().image().original()), data.score())
                        )
                        .collect(Collectors.toSet()),
                shikimoriListExtractor.getToInclude()
        );
        Assertions.assertEquals(
                Stream.of(droppedJson, completedJson, watchingJson, onHoldJson)
                        .flatMap(Collection::stream)
                        .map(
                                data -> new UserAnimeTitle(new AnimeTitle(data.anime().id(), data.anime().name(), "https://shikimori.one" + data.anime().image().original()), data.score())
                        )
                        .collect(Collectors.toSet()),
                shikimoriListExtractor.getToExclude()
        );
    }
}

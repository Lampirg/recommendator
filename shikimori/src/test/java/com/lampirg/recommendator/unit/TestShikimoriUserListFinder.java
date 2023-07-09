package com.lampirg.recommendator.unit;

import com.lampirg.recommendator.anidb.json.Image;
import com.lampirg.recommendator.anidb.json.ShikiNode;
import com.lampirg.recommendator.anidb.json.ShikiUserNode;
import com.lampirg.recommendator.anidb.query.ShikimoriQueryMaker;
import com.lampirg.recommendator.anidb.query.ShikimoriUserListFinder;
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
import java.util.stream.Stream;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TestShikimoriUserListFinder {
    @Mock
    private ShikimoriQueryMaker shikimoriQueryMaker;
    @InjectMocks
    private ShikimoriUserListFinder shikimoriUserListFinder;

    private final List<ShikiUserNode> jsonWithoutNextPage = List.of(
            new ShikiUserNode(10, new ShikiNode(1, "Hadaske", new Image("/notfound"))),
            new ShikiUserNode(10, new ShikiNode(2, "Hadaske: Return to Omsk", new Image("/totally-notfound")))
    );


    private final List<ShikiUserNode> jsonWithNextPage = Stream
            .generate(() -> new ShikiUserNode(10, new ShikiNode(3, "DeHadaske", new Image("/ccv"))))
            .limit(101)
            .toList();

    @Test
    void givenNoPaging() {
        when(shikimoriQueryMaker.exchange(Mockito.contains("completed"), Mockito.eq(HttpMethod.GET), Mockito.eq(new ParameterizedTypeReference<List<ShikiUserNode>>() {
        }))).thenReturn(ResponseEntity.of(Optional.of(jsonWithoutNextPage)));
        Assertions.assertEquals(
                jsonWithoutNextPage,
                shikimoriUserListFinder.findUserList("lampirg", "completed")
        );
    }

    @Test
    void givenPaging() {
        when(shikimoriQueryMaker.exchange(Mockito.contains("page=1"), Mockito.eq(HttpMethod.GET), Mockito.eq(new ParameterizedTypeReference<List<ShikiUserNode>>() {
        }))).thenReturn(ResponseEntity.of(Optional.of(jsonWithNextPage)));
        when(shikimoriQueryMaker.exchange(Mockito.contains("page=2"), Mockito.eq(HttpMethod.GET), Mockito.eq(new ParameterizedTypeReference<List<ShikiUserNode>>() {
        }))).thenReturn(ResponseEntity.of(Optional.of(jsonWithoutNextPage)));
        Assertions.assertEquals(
                Stream.of(jsonWithNextPage, jsonWithoutNextPage)
                        .flatMap(Collection::stream)
                        .toList(),
                shikimoriUserListFinder.findUserList("lampirg", "completed")
        );
    }
}

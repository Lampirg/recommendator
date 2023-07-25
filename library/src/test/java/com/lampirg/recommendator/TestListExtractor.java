package com.lampirg.recommendator;

import com.lampirg.recommendator.anidb.general.listextractor.StandardListExtractor;
import com.lampirg.recommendator.anidb.titles.model.AnimeTitle;
import com.lampirg.recommendator.anidb.titles.model.UserAnimeTitle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class TestListExtractor {

    @Spy
    private StandardListExtractor extractor;

    private List<UserAnimeTitle> titles = List.of(
            new UserAnimeTitle(new AnimeTitle(1, "Hadaske", "/notfound"), 10),
            new UserAnimeTitle(new AnimeTitle(2, "Hadaske: Return to Omsk", "/totally-notfound"), 1),
            new UserAnimeTitle(new AnimeTitle(3, "DeHadaske", "/no"), 10),
            new UserAnimeTitle(new AnimeTitle(4, "Hadaske - Kukic", "/notfound"), 8)
    );

    @Test
    void testToIncludeAndToExclude() {
        Mockito.when(extractor.getUserCompletedAnimeList("lampirg"))
                .thenReturn(Set.of(titles.get(0)));
        Mockito.when(extractor.getUserWatchingAnimeList("lampirg"))
                .thenReturn(Set.of(titles.get(1)));
        Mockito.when(extractor.getUserDroppedAnimeList("lampirg"))
                .thenReturn(Set.of(titles.get(2)));
        Mockito.when(extractor.getUserOnHoldAnimeList("lampirg"))
                .thenReturn(Set.of(titles.get(3)));
        Assertions.assertEquals(new HashSet<>(titles), extractor.getToExclude("lampirg"));
        Assertions.assertEquals(Set.of(titles.get(0)), extractor.getToInclude("lampirg"));
    }
}

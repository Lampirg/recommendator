package com.lampirg.recommendator.unit;

import com.lampirg.recommendator.anidb.ShikimoriCacher;
import com.lampirg.recommendator.anidb.ShikimoriTitleMapper;
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

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class TestTitleMapper {

    @Mock
    ShikimoriCacher malCacher;
    @InjectMocks
    ShikimoriTitleMapper shikimoriTitleMapper;

    private final List<AnimeTitle> titles = List.of(
            new AnimeTitle(1, "Hadaske", "/notfound"),
            new AnimeTitle(2, "Hadaske: Return to Omsk", "/totally-notfound"),
            new AnimeTitle(3, "DeHadaske", "/no"),
            new AnimeTitle(4, "Hadaske - Kukic", "/notfound")
    );

    private final Map<AnimeTitle, Set<AnimeTitle>> recommendations = Map.ofEntries(
            Map.entry(titles.get(0),
                    Set.of(
                            titles.get(2),
                            titles.get(3)
                    )),
            Map.entry(titles.get(1), Set.of(titles.get(2)))
    );

    private final Set<UserAnimeTitle> userTitles = Set.of(
            new UserAnimeTitle(titles.get(0), 10),
            new UserAnimeTitle(titles.get(1), 1)
    );
    private final Set<UserAnimeTitle> toExclude = Set.of(new UserAnimeTitle(titles.get(2), 5));

    @Test
    @DisplayName("Test when exclude list is empty")
    void notGivenToExclude() {
        Mockito.when(malCacher.getRecommendations(Mockito.any()))
                .then(onMock -> recommendations.get((AnimeTitle) onMock.getArgument(0)));
        shikimoriTitleMapper.fillToExclude(Set.of());
        var result = shikimoriTitleMapper.getRecommendedAnimeMap(userTitles);
        Assertions.assertEquals(11, result.get(titles.get(2)));
        Assertions.assertEquals(10, result.get(titles.get(3)));
    }

    @Test
    @DisplayName("Test when given not empty exclude list")
    void givenToExclude() {
        Mockito.when(malCacher.getRecommendations(Mockito.any()))
                .then(onMock -> recommendations.get((AnimeTitle) onMock.getArgument(0)));
        shikimoriTitleMapper.fillToExclude(toExclude);
        var result = shikimoriTitleMapper.getRecommendedAnimeMap(userTitles);
        Assertions.assertNull(result.get(titles.get(2)));
        Assertions.assertEquals(10, result.get(titles.get(3)));
    }

    @Test
    @DisplayName("Test caching")
    void whenSecondTime() {
        Mockito.when(malCacher.getRecommendations(Mockito.any()))
                .then(onMock -> recommendations.get((AnimeTitle) onMock.getArgument(0)));
        shikimoriTitleMapper.fillToExclude(Set.of());
        var result = shikimoriTitleMapper.getRecommendedAnimeMap(userTitles);
        Assertions.assertEquals(result, Assertions.assertTimeoutPreemptively(Duration.ofMillis(1), () -> shikimoriTitleMapper.getRecommendedAnimeMap(userTitles)));
        Assertions.assertEquals(11, result.get(titles.get(2)));
        Assertions.assertEquals(10, result.get(titles.get(3)));
    }
}

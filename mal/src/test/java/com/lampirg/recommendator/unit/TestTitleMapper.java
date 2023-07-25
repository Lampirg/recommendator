package com.lampirg.recommendator.unit;

import com.lampirg.recommendator.anidb.query.MalRecommendationsFinder;
import com.lampirg.recommendator.anidb.titlemapper.ConcurrentTitleMapper;
import com.lampirg.recommendator.anidb.titlemapper.SingleThreadTitleMapper;
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
    MalRecommendationsFinder malRecommendationsFinder;
    @InjectMocks
    SingleThreadTitleMapper singleThreadTitleMapper;
    @InjectMocks
    ConcurrentTitleMapper concurrentTitleMapper;

    private final List<AnimeTitle> titles = List.of(
            new AnimeTitle(1, "Hadaske", "/notfound"),
            new AnimeTitle(2, "Hadaske: Return to Omsk", "/totally-notfound"),
            new AnimeTitle(3, "DeHadaske", "/no"),
            new AnimeTitle(4, "Hadaske - Kukic", "/notfound")
    );

    private final Map<AnimeTitle, Set<AnimeTitle>> reccommendations = Map.ofEntries(
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
    @DisplayName("Test TitleMapper classes without anime titles to exclude")
    void notGivenToExclude() {
        Mockito.when(malRecommendationsFinder.findRecommendations(Mockito.any()))
                .then(onMock -> reccommendations.get((AnimeTitle) onMock.getArgument(0)));
        singleThreadTitleMapper.fillToExclude(Set.of());
        var result = singleThreadTitleMapper.getRecommendedAnimeMap(userTitles);
        Assertions.assertEquals(11, result.get(titles.get(2)));
        Assertions.assertEquals(10, result.get(titles.get(3)));
        concurrentTitleMapper.fillToExclude(Set.of());
        result = concurrentTitleMapper.getRecommendedAnimeMap(userTitles);
        Assertions.assertEquals(11, result.get(titles.get(2)));
        Assertions.assertEquals(10, result.get(titles.get(3)));
    }

    @Test
    @DisplayName("Test TitleMapper classes with anime titles to exclude")
    void givenToExclude() {
        Mockito.when(malRecommendationsFinder.findRecommendations(Mockito.any()))
                .then(onMock -> reccommendations.get((AnimeTitle) onMock.getArgument(0)));
        singleThreadTitleMapper.fillToExclude(toExclude);
        var result = singleThreadTitleMapper.getRecommendedAnimeMap(userTitles);
        Assertions.assertNull(result.get(titles.get(2)));
        Assertions.assertEquals(10, result.get(titles.get(3)));
        concurrentTitleMapper.fillToExclude(toExclude);
        result = concurrentTitleMapper.getRecommendedAnimeMap(userTitles);
        Assertions.assertNull(result.get(titles.get(2)));
        Assertions.assertEquals(10, result.get(titles.get(3)));
    }

    // TODO: remake this test as it violate Repeatable principle in F.I.R.S.T. acronym
    // (also asserting speed using timeout is obviously bad idea; instead probably spy method in Mockito should be used)
    @Test
    @DisplayName("Test second call of getRecommendedAnimeMap method in TitleMapper classes")
    void whenSecondTime() {
        Mockito.when(malRecommendationsFinder.findRecommendations(Mockito.any()))
                .then(onMock -> reccommendations.get((AnimeTitle) onMock.getArgument(0)));
        singleThreadTitleMapper.fillToExclude(Set.of());
        var result = singleThreadTitleMapper.getRecommendedAnimeMap(userTitles);
        Assertions.assertEquals(result, Assertions.assertTimeoutPreemptively(
                Duration.ofMillis(1), () -> singleThreadTitleMapper.getRecommendedAnimeMap(userTitles)
        ));
        Assertions.assertEquals(11, result.get(titles.get(2)));
        Assertions.assertEquals(10, result.get(titles.get(3)));
        concurrentTitleMapper.fillToExclude(Set.of());
        result = concurrentTitleMapper.getRecommendedAnimeMap(userTitles);
        Assertions.assertEquals(result, Assertions.assertTimeoutPreemptively(
                Duration.ofMillis(1), () -> concurrentTitleMapper.getRecommendedAnimeMap(userTitles)
        ));
        Assertions.assertEquals(11, result.get(titles.get(2)));
        Assertions.assertEquals(10, result.get(titles.get(3)));
    }
}

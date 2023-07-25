package com.lampirg.recommendator;

import com.lampirg.recommendator.anidb.general.SimilarAnimeCommunicator;
import com.lampirg.recommendator.anidb.general.listextractor.UserListExtractor;
import com.lampirg.recommendator.anidb.general.titlemapper.TitleMapper;
import com.lampirg.recommendator.anidb.titles.model.AnimeRecommendation;
import com.lampirg.recommendator.anidb.titles.model.AnimeTitle;
import com.lampirg.recommendator.anidb.titles.model.UserAnimeTitle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
public class TestCommunicator {
    @Mock
    private UserListExtractor userListExtractor;
    @Mock
    private TitleMapper titleMapper;
    @InjectMocks
    private SimilarAnimeCommunicator communicator;

    private Set<UserAnimeTitle> titles = Set.of(
            new UserAnimeTitle(new AnimeTitle(1, "Hadaske", "/notfound"), 10),
            new UserAnimeTitle(new AnimeTitle(2, "Hadaske: Return to Omsk", "/totally-notfound"), 1)
    );

    @Test
    void findTitles() {
        Mockito.when(userListExtractor.getToInclude("lampirg")).thenReturn(titles);
        Mockito.when(userListExtractor.getToExclude("lampirg")).thenReturn(Set.of());
        Mockito.when(titleMapper.fillToExclude(Mockito.anySet())).thenReturn(titleMapper);
        Mockito.when(titleMapper.getRecommendedAnimeMap(titles))
                .thenReturn(getMapFromSet(titles));
        Assertions.assertEquals(
                titles
                        .stream()
                        .map(title -> new AnimeRecommendation(title.animeTitle(), 6))
                        .collect(Collectors.toSet()),
                communicator.getSimilarAnimeTitles("lampirg")
        );
    }

    private static Map<AnimeTitle, Integer> getMapFromSet(Set<UserAnimeTitle> toInclude) {
        Map<AnimeTitle, Integer> result = new HashMap<>();
        toInclude.forEach(title -> result.put(title.animeTitle(), 6));
        return result;
    }
}

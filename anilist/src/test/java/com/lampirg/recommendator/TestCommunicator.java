package com.lampirg.recommendator;

import com.lampirg.recommendator.anidb.AnilistCommunicator;
import com.lampirg.recommendator.anidb.AnilistQueryMaker;
import com.lampirg.recommendator.anidb.json.Completed;
import com.lampirg.recommendator.anidb.json.GetUserListAndRecommendations;
import com.lampirg.recommendator.anidb.json.Other;
import com.lampirg.recommendator.anidb.titles.model.AnimeRecommendation;
import com.lampirg.recommendator.anidb.titles.model.AnimeTitle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class TestCommunicator {
    @Mock
    private AnilistQueryMaker queryMaker;
    @InjectMocks
    private AnilistCommunicator anilistCommunicator;

    private final List<Completed.CompletedList.Entries> completed = List.of(
            new Completed.CompletedList.Entries(
                    new Completed.CompletedList.Entries.Media(
                            1,
                            new Completed.CompletedList.Entries.Media.Title("Hadaske"),
                            new Completed.CompletedList.Entries.Media.CoverImage("/extra", "l", "m"),
                            new Completed.CompletedList.Entries.Media.Recommendations(List.of(
                                    new Completed.CompletedList.Entries.Media.Recommendations.Nodes(
                                            4,
                                            new Completed.CompletedList.Entries.Media.Recommendations.Nodes.MediaRecommendation(
                                                    3,
                                                    new Completed.CompletedList.Entries.Media.Title("DeHadaske"),
                                                    new Completed.CompletedList.Entries.Media.CoverImage("/el", "/l", "/m")
                                            )
                                    ),
                                    new Completed.CompletedList.Entries.Media.Recommendations.Nodes(
                                            6,
                                            new Completed.CompletedList.Entries.Media.Recommendations.Nodes.MediaRecommendation(
                                                    4,
                                                    new Completed.CompletedList.Entries.Media.Title("Hadaske - Kukic"),
                                                    new Completed.CompletedList.Entries.Media.CoverImage("/el", "/l", "/m")
                                            )
                                    )
                            ))
                    ),
                    10
            ),
            new Completed.CompletedList.Entries(
                    new Completed.CompletedList.Entries.Media(
                            2,
                            new Completed.CompletedList.Entries.Media.Title("Hadaske"),
                            new Completed.CompletedList.Entries.Media.CoverImage("/extra", "l", "m"),
                            new Completed.CompletedList.Entries.Media.Recommendations(List.of(
                                    new Completed.CompletedList.Entries.Media.Recommendations.Nodes(
                                            6,
                                            new Completed.CompletedList.Entries.Media.Recommendations.Nodes.MediaRecommendation(
                                                    4,
                                                    new Completed.CompletedList.Entries.Media.Title("Hadaske - Kukic"),
                                                    new Completed.CompletedList.Entries.Media.CoverImage("/el", "/l", "/m")
                                            )
                                    )
                            ))
                    ),
                    1
            )
    );

    private final List<Other.Lists.Entries> other = List.of(
            new Other.Lists.Entries(
                    new Other.Lists.Entries.Media(4,
                            new Other.Lists.Entries.Media.Title("Hadaske - Kukic"),
                            new Other.Lists.Entries.Media.CoverImage("/el", "/l", "/m")
                    ),
                    1
            )
    );

    private final GetUserListAndRecommendations firstJson = new GetUserListAndRecommendations(
            new GetUserListAndRecommendations.Data(
                    new Completed(List.of(new Completed.CompletedList("COMPLETED", completed))),
                    new Other(List.of(
                            new Other.Lists("CURRENT", List.of()),
                            new Other.Lists("DROPPED", List.of()),
                            new Other.Lists("PAUSED", List.of())
                    ))
            )
    );

    private final GetUserListAndRecommendations secondJson = new GetUserListAndRecommendations(
            new GetUserListAndRecommendations.Data(
                    new Completed(List.of(new Completed.CompletedList("COMPLETED", completed))),
                    new Other(List.of(
                            new Other.Lists("CURRENT", List.of()),
                            new Other.Lists("DROPPED", other),
                            new Other.Lists("PAUSED", List.of())
                    ))
            )
    );

    @Test
    void givenNoToExclude() {
        Mockito.when(queryMaker.exchange(
                Mockito.eq(URI.create("https://graphql.anilist.co")),
                Mockito.eq(HttpMethod.POST),
                Mockito.any(),
                Mockito.eq(GetUserListAndRecommendations.class)
        )).thenReturn(ResponseEntity.of(Optional.of(firstJson)));
        Assertions.assertEquals(
                Set.of(
                        new AnimeRecommendation(
                                new AnimeTitle(
                                        3, "DeHadaske", "/el"
                                ),
                                10
                        ),
                        new AnimeRecommendation(
                                new AnimeTitle(
                                        4, "Hadaske - Kukic", "/el"
                                ),
                                11
                        )
                ),
                anilistCommunicator.getSimilarAnimeTitles("lampirg")
        );
    }

    @Test
    void givenToExclude() {
        Mockito.when(queryMaker.exchange(
                Mockito.eq(URI.create("https://graphql.anilist.co")),
                Mockito.eq(HttpMethod.POST),
                Mockito.any(),
                Mockito.eq(GetUserListAndRecommendations.class)
        )).thenReturn(ResponseEntity.of(Optional.of(secondJson)));
        Assertions.assertEquals(
                Set.of(
                        new AnimeRecommendation(
                                new AnimeTitle(
                                        3, "DeHadaske", "/el"
                                ),
                                10
                        )
                ),
                anilistCommunicator.getSimilarAnimeTitles("lampirg")
        );
    }
}

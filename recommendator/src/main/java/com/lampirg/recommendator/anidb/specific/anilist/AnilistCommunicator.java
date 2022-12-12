package com.lampirg.recommendator.anidb.specific.anilist;

import com.lampirg.recommendator.anidb.specific.anilist.json.Completed;
import com.lampirg.recommendator.anidb.specific.anilist.json.GetUserListAndRecommendations;
import com.lampirg.recommendator.anidb.specific.anilist.json.GraphQlRequest;
import com.lampirg.recommendator.anidb.specific.anilist.json.Other;
import com.lampirg.recommendator.anidb.general.AnimeSiteCommunicator;
import com.lampirg.recommendator.anidb.general.model.AnimeRecommendation;
import com.lampirg.recommendator.anidb.general.model.AnimeTitle;
import com.lampirg.recommendator.anidb.general.model.UserAnimeTitle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;


public class AnilistCommunicator implements AnimeSiteCommunicator {

    private AnilistQueryMaker queryMaker;

    private HttpEntity<GraphQlRequest> request;

    private Resource resource = new ClassPathResource("query.graphql");

    @Autowired
    public void setQueryMaker(AnilistQueryMaker queryMaker) {
        this.queryMaker = queryMaker;
    }

    @Override
    public Set<AnimeRecommendation> getSimilarAnimeTitles(String username) {
        ResponseEntity<GetUserListAndRecommendations> response = makeAnilistQuery(username);
        Set<AnimeTitle> toExclude = getToExclude(response);
        Map<AnimeTitle, Integer> recommendationMap = getRecommendationMap(response, toExclude);
        Set<AnimeRecommendation> animeRecommendationSet = new HashSet<>();
        recommendationMap.forEach((key, value) -> animeRecommendationSet.add(new AnimeRecommendation(key, value)));
        return Set.copyOf(animeRecommendationSet);
    }

    private ResponseEntity<GetUserListAndRecommendations> makeAnilistQuery(String username) {
        String query;
        try (BufferedReader stream = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)
        )) {
            query = stream.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        request = new HttpEntity<>(new GraphQlRequest(query, String.format(GetUserListAndRecommendations.variables, username)));
        ResponseEntity<GetUserListAndRecommendations> response = queryMaker.exchange(
                URI.create("https://graphql.anilist.co"),
                HttpMethod.POST,
                request,
                GetUserListAndRecommendations.class
        );
        return response;
    }

    private Set<AnimeTitle> getToExclude(ResponseEntity<GetUserListAndRecommendations> response) {
        Map<String, Set<UserAnimeTitle>> statusMap = new HashMap<>();
        for (Other.Lists lists : Objects.requireNonNull(response.getBody()).data().other().lists()) {
            statusMap.put(lists.status(), new HashSet<>());
            Set<UserAnimeTitle> set = statusMap.get(lists.status());
            for (Other.Lists.Entries entries : lists.entries()) {
                set.add(new UserAnimeTitle(AnimeTitle.retrieveFromAnilistMedia(entries.media()), entries.score()));
            }
        }

        Set<AnimeTitle> toExclude = new HashSet<>();
        for (Set<UserAnimeTitle> set : statusMap.values()) {
            for (UserAnimeTitle title: set) {
                toExclude.add(title.animeTitle());
            }
        }
        return toExclude;
    }

    private Map<AnimeTitle, Integer> getRecommendationMap(ResponseEntity<GetUserListAndRecommendations> response, Set<AnimeTitle> toExclude) {
        Map<AnimeTitle, Integer> recommendationMap = new HashMap<>();
        for (Completed.CompletedList.Entries entries : Objects.requireNonNull(response.getBody()).data().completed().lists().get(0).entries()) {
            int score = entries.score() != 0 ? entries.score() : 1;
            UserAnimeTitle completedTitle = new UserAnimeTitle(AnimeTitle.retrieveFromAnilistMedia(entries.media()), score);
            toExclude.add(completedTitle.animeTitle());
            for (Completed.CompletedList.Entries.Media.Recommendations.Nodes nodes :
                    entries.media().recommendations().nodes()) {
                if (nodes.mediaRecommendation() == null || nodes.rating() <= 0)
                    continue;
                AnimeTitle recommendedTitle = AnimeTitle.retrieveFromAnilistMedia(nodes.mediaRecommendation());
                recommendationMap.merge(recommendedTitle, completedTitle.score(), Integer::sum);
            }
        }
        recommendationMap.keySet().removeAll(toExclude);
        return recommendationMap;
    }
}

package com.lampirg.recommendator.anidb;

import com.lampirg.recommendator.anidb.json.GraphQlRequest;
import com.lampirg.recommendator.anidb.json.Completed;
import com.lampirg.recommendator.anidb.json.GetUserListAndRecommendations;
import com.lampirg.recommendator.anidb.json.Other;
import com.lampirg.recommendator.anidb.general.AnimeSiteCommunicator;
import com.lampirg.recommendator.anidb.titles.model.AnimeRecommendation;
import com.lampirg.recommendator.anidb.titles.model.AnimeTitle;
import com.lampirg.recommendator.anidb.titles.model.UserAnimeTitle;
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
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class AnilistCommunicator implements AnimeSiteCommunicator {

    private AnilistQueryMaker queryMaker;
    // TODO: remove instantiating according to DI paradigm (and mock it in unit test)
    private final Resource resource = new ClassPathResource("query.graphql");

    @Autowired
    public void setQueryMaker(AnilistQueryMaker queryMaker) {
        this.queryMaker = queryMaker;
    }

    @Override
    public Set<AnimeRecommendation> getSimilarAnimeTitles(String username) {
        ResponseEntity<GetUserListAndRecommendations> response = makeAnilistQuery(username);
        return getRecommendationMap(response, getToExclude(response))
                .entrySet()
                .stream()
                .map(entry -> new AnimeRecommendation(entry.getKey(), entry.getValue()))
                .collect(Collectors.toUnmodifiableSet());
    }

    private ResponseEntity<GetUserListAndRecommendations> makeAnilistQuery(String username) {
        HttpEntity<GraphQlRequest> request = new HttpEntity<>(new GraphQlRequest(
                Utils.resourceToString(resource),
                String.format(GetUserListAndRecommendations.variables, username)
        ));
        return queryMaker.exchange(
                URI.create("https://graphql.anilist.co"),
                HttpMethod.POST,
                request,
                GetUserListAndRecommendations.class
        );
    }

    private Set<AnimeTitle> getToExclude(ResponseEntity<GetUserListAndRecommendations> response) {
        Stream<AnimeTitle> completed = response.getBody().data().completed().lists().get(0).entries().stream()
                .map(entries -> Utils.retrieveFromAnilistMedia(entries.media()));
        Stream<AnimeTitle> other = response.getBody().data().other().lists().stream()
                .map(Other.Lists::entries)
                .flatMap(Collection::stream)
                .map(entries -> Utils.retrieveFromAnilistMedia(entries.media()));
        return Stream.concat(completed, other).collect(Collectors.toUnmodifiableSet());
    }

    private Map<AnimeTitle, Integer> getRecommendationMap(ResponseEntity<GetUserListAndRecommendations> response, Set<AnimeTitle> toExclude) {
        Map<AnimeTitle, Integer> recommendationMap = new HashMap<>();
        response.getBody().data().completed().lists().get(0).entries().stream()
                .map(mapUserTitleToRecommendations(toExclude))
                .forEach(entry -> entry.getValue().forEach(
                        recommendation -> recommendationMap.merge(recommendation, entry.getKey().score(), Integer::sum)
                ));
        return recommendationMap;
    }

    private Function<Completed.CompletedList.Entries, Map.Entry<UserAnimeTitle, Set<AnimeTitle>>> mapUserTitleToRecommendations(Set<AnimeTitle> toExclude) {
        return entries -> Map.entry(
                new UserAnimeTitle(Utils.retrieveFromAnilistMedia(entries.media()), entries.score()),
                entries.media().recommendations().nodes().stream()
                        .filter(nodes -> nodes.mediaRecommendation() != null && nodes.rating() > 0)
                        .map(nodes -> Utils.retrieveFromAnilistMedia(nodes.mediaRecommendation()))
                        .filter(title -> !toExclude.contains(title))
                        .collect(Collectors.toUnmodifiableSet())
        );
    }
}

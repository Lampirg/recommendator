package com.lampirg.recommendator.anidb;

import com.lampirg.recommendator.anidb.json.queries.GetAnimeDetail;
import com.lampirg.recommendator.anidb.json.Recommendation;
import com.lampirg.recommendator.anidb.titles.model.AnimeTitle;
import com.lampirg.recommendator.anidb.titles.repository.AnimeRecommendationsCacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MalRecommendationsExtractor implements AnimeRecommendationsCacher {

    private MalQueryMaker queryMaker;

    @Autowired
    public void setQueryMaker(MalQueryMaker queryMaker) {
        this.queryMaker = queryMaker;
    }

    @Cacheable("mal-recommendations")
    public Set<AnimeTitle> findRecommendations(AnimeTitle title) {
        String url = "https://api.myanimelist.net/v2/anime/"+title.id()+"?fields=recommendations";
        ResponseEntity<GetAnimeDetail> response = queryMaker.exchange(url, HttpMethod.GET, GetAnimeDetail.class);
        return Objects.requireNonNull(response.getBody()).recommendations().stream()
                .map(recommendation -> Utils.retrieveFromMalNode(recommendation.node()))
                .collect(Collectors.toSet());
    }
}

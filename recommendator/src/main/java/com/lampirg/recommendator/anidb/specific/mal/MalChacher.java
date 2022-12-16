package com.lampirg.recommendator.anidb.specific.mal;

import com.lampirg.recommendator.anidb.specific.mal.json.Recommendation;
import com.lampirg.recommendator.anidb.specific.mal.json.queries.GetAnimeDetail;
import com.lampirg.recommendator.anidb.titles.model.AnimeTitle;
import com.lampirg.recommendator.anidb.titles.repository.AnimeRecommendationsCacher;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
public class MalChacher implements AnimeRecommendationsCacher {

    @Cacheable("mal-recommendations")
    public Set<AnimeTitle> getRecommendations(AnimeTitle title, MalQueryMaker queryMaker) {
        String url = "https://api.myanimelist.net/v2/anime/"+title.id()+"?fields=recommendations";
        ResponseEntity<GetAnimeDetail> response = queryMaker.exchange(url, HttpMethod.GET, GetAnimeDetail.class);
        Set<AnimeTitle> recommendedTitles = new HashSet<>();
        for (Recommendation recommendation : Objects.requireNonNull(response.getBody()).recommendations()) {
            recommendedTitles.add(AnimeTitle.retrieveFromMalNode(recommendation.node()));
        }
        return recommendedTitles;
    }
}

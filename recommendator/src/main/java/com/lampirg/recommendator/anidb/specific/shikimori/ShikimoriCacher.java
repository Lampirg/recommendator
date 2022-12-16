package com.lampirg.recommendator.anidb.specific.shikimori;

import com.lampirg.recommendator.anidb.specific.shikimori.json.ShikiNode;
import com.lampirg.recommendator.anidb.titles.model.AnimeTitle;
import com.lampirg.recommendator.anidb.titles.repository.AnimeRecommendationsCacher;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class ShikimoriCacher implements AnimeRecommendationsCacher {

    @Cacheable("shikimori-recommendations")
    public Set<AnimeTitle> getRecommendations(AnimeTitle title, ShikimoriQueryMaker queryMaker) {
        String url = "https://shikimori.one/api/animes/"+title.id()+"/similar";
        ResponseEntity<List<ShikiNode>> response = queryMaker.exchange(url, HttpMethod.GET, new ParameterizedTypeReference<>() {});
        Set<AnimeTitle> recommendedTitles = new HashSet<>();
        for (ShikiNode recommendation : Objects.requireNonNull(response.getBody())) {
            recommendedTitles.add(AnimeTitle.retrieveFromShikiNode(recommendation));
        }
        return recommendedTitles;
    }
}

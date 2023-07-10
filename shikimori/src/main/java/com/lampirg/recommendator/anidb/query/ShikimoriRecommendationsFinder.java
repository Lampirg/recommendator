package com.lampirg.recommendator.anidb.query;

import com.lampirg.recommendator.anidb.Utils;
import com.lampirg.recommendator.anidb.json.ShikiNode;
import com.lampirg.recommendator.anidb.titles.model.AnimeTitle;
import com.lampirg.recommendator.anidb.titles.repository.AnimeRecommendationsCacher;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ShikimoriRecommendationsFinder implements AnimeRecommendationsCacher {

    private ShikimoriQueryMaker queryMaker;

    @Autowired
    public void setQueryMaker(ShikimoriQueryMaker queryMaker) {
        this.queryMaker = queryMaker;
    }

    @Cacheable("shikimori-recommendations")
    public Set<AnimeTitle> getRecommendations(AnimeTitle title) {
        String url = "https://shikimori.one/api/animes/"+title.id()+"/similar";
        ResponseEntity<List<ShikiNode>> response = queryMaker.exchange(url, HttpMethod.GET, new ParameterizedTypeReference<>() {});
        Set<AnimeTitle> recommendedTitles = new HashSet<>();
        for (ShikiNode recommendation : Objects.requireNonNull(response.getBody())) {
            recommendedTitles.add(Utils.retrieveFromShikiNode(recommendation));
        }
        return recommendedTitles;
    }
}

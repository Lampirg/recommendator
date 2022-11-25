package com.lampirg.recommendator.anidb.mal.titlemapper;

import com.lampirg.recommendator.anidb.mal.json.Recommendation;
import com.lampirg.recommendator.anidb.mal.json.queries.GetAnimeDetail;
import com.lampirg.recommendator.model.AnimeTitle;
import com.lampirg.recommendator.model.UserAnimeTitle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component("singleThread")
public class SingleThreadTitleMapper implements TitleMapper {

    RestTemplate restTemplate;
    HttpEntity<String> request;

    private final Map<AnimeTitle, Integer> recommendedAnime = new HashMap<>();
    private Set<AnimeTitle> toExclude = new HashSet<>();
    private final static long DELAY = 500;
    private long startTime;

    @Override
    public Map<AnimeTitle, Integer> getRecommendedAnimeMap() {
        return recommendedAnime;
    }

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void setRequest(HttpEntity<String> request) {
        this.request = request;
    }

    public void fillToExclude(Set<UserAnimeTitle> animeTitles) {
        for (UserAnimeTitle title : animeTitles) {
            toExclude.add(title.animeTitle());
        }
    }

    public void findAndAddTitleRecommendations(UserAnimeTitle title) {
        String url = "https://api.myanimelist.net/v2/anime/"+title.animeTitle().id()+"?fields=recommendations";
        while (System.currentTimeMillis() - startTime < DELAY)
            Thread.onSpinWait();
        ResponseEntity<GetAnimeDetail> response = restTemplate.exchange(url, HttpMethod.GET, request, GetAnimeDetail.class);
        startTime = System.currentTimeMillis();
        for (Recommendation recommendation : Objects.requireNonNull(response.getBody()).recommendations()) {
            AnimeTitle animeTitle = AnimeTitle.retrieveFromMalNode(recommendation.node());
            if (toExclude.contains(animeTitle))
                continue;
            recommendedAnime.merge(animeTitle, title.score(), Integer::sum);
        }
    }
}

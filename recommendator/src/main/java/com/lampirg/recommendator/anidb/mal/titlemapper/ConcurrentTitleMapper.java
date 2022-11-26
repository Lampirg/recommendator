package com.lampirg.recommendator.anidb.mal.titlemapper;

import com.lampirg.recommendator.anidb.mal.json.Recommendation;
import com.lampirg.recommendator.anidb.mal.json.queries.GetAnimeDetail;
import com.lampirg.recommendator.model.AnimeTitle;
import com.lampirg.recommendator.model.UserAnimeTitle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
// TODO: fix thread unsafety
@Component("concurrent")
@Scope("prototype")
public class ConcurrentTitleMapper implements TitleMapper {

    RestTemplate restTemplate;
    HttpEntity<String> request;

    private final Map<AnimeTitle, Integer> recommendedAnime = new ConcurrentHashMap<>();
    private Set<AnimeTitle> toExclude = new HashSet<>();
    DelayQueue<DelayedResponse> delayQueue = new DelayQueue<>();
    private long startTime;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public TitleMapper setRequest(HttpEntity<String> request) {
        this.request = request;
        return this;
    }

    @Override
    public Map<AnimeTitle, Integer> getRecommendedAnimeMap(Set<UserAnimeTitle> animeTitles) {
        if (!recommendedAnime.isEmpty())
            return recommendedAnime;
        // TODO: add concurrency here
        for (UserAnimeTitle title : animeTitles) {
            findAndAddTitleRecommendations(title);
        }
        return recommendedAnime;
    }

    public TitleMapper fillToExclude(Set<UserAnimeTitle> toExclude) {
        for (UserAnimeTitle title : toExclude) {
            this.toExclude.add(title.animeTitle());
        }
        return this;
    }

    public void findAndAddTitleRecommendations(UserAnimeTitle title) {
        String url = "https://api.myanimelist.net/v2/anime/"+title.animeTitle().id()+"?fields=recommendations";
        delayQueue.add(new DelayedResponse(url));
        ResponseEntity<GetAnimeDetail> response = null;
        try {
            response = restTemplate.exchange(
                    delayQueue.take().url, HttpMethod.GET, request, GetAnimeDetail.class);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        startTime = System.currentTimeMillis();
        for (Recommendation recommendation : Objects.requireNonNull(response.getBody()).recommendations()) {
            AnimeTitle animeTitle = AnimeTitle.retrieveFromMalNode(recommendation.node());
            if (toExclude.contains(animeTitle))
                continue;
            recommendedAnime.merge(animeTitle, title.score(), Integer::sum);
        }
    }

    private class DelayedResponse implements Delayed {

        private final String url;
        private final static int DELAY = 500;
        public DelayedResponse(String url) {
            this.url = url;
        }
        @Override
        public long getDelay(TimeUnit unit) {
            long dif = startTime + DELAY - System.currentTimeMillis();
            return unit.convert(dif, TimeUnit.MILLISECONDS);
        }
        @Override
        public int compareTo(Delayed o) {
            return 0;
        }
    }
}

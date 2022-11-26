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
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

// TODO: fix thread unsafety
@Component
@Qualifier("concurrent")
@Scope("prototype")
public class ConcurrentTitleMapper implements TitleMapper {

    RestTemplate restTemplate;
    HttpEntity<String> request;

    private final Map<AnimeTitle, Integer> recommendedAnime = new ConcurrentHashMap<>();
    private Set<AnimeTitle> toExclude = new HashSet<>();
    private DelayQueue<DelayedResponse> delayQueue = new DelayQueue<>();
    private volatile long startTime;
    private ReentrantLock lock = new ReentrantLock();

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
        CompletableFuture<Void> future = CompletableFuture.allOf();
        for (UserAnimeTitle title : animeTitles) {
            future = CompletableFuture.allOf(future,
                    CompletableFuture.runAsync(() -> findAndAddTitleRecommendations(title))
            );
        }
        future.join();
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
        response = restTemplate.exchange(
                getUrlFromQueue(), HttpMethod.GET, request, GetAnimeDetail.class);
        for (Recommendation recommendation : Objects.requireNonNull(response.getBody()).recommendations()) {
            AnimeTitle animeTitle = AnimeTitle.retrieveFromMalNode(recommendation.node());
            if (toExclude.contains(animeTitle))
                continue;
            recommendedAnime.merge(animeTitle, title.score(), Integer::sum);
        }
    }

    private String getUrlFromQueue() {
        try {
            lock.lock();
            String url = delayQueue.take().url;
            startTime = System.currentTimeMillis();
            return url;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
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

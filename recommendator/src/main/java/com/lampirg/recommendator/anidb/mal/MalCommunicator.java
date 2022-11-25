package com.lampirg.recommendator.anidb.mal;

import com.lampirg.recommendator.anidb.AnimeSiteCommunicator;
import com.lampirg.recommendator.anidb.mal.json.Data;
import com.lampirg.recommendator.anidb.mal.json.Recommendation;
import com.lampirg.recommendator.anidb.mal.json.queries.GetAnimeDetail;
import com.lampirg.recommendator.anidb.mal.json.queries.GetUserListJsonResult;
import com.lampirg.recommendator.model.AnimeRecommendation;
import com.lampirg.recommendator.model.AnimeTitle;
import com.lampirg.recommendator.model.UserAnimeTitle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.*;

@Service
@PropertySource("classpath:mal security code.yml")
public class MalCommunicator implements AnimeSiteCommunicator {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${clientIdHeader}")
    private String clientIdHeader;
    @Value("${clientId}")
    private String clientId;
    private HttpHeaders authHeader;
    HttpEntity<String> request;

    @PostConstruct
    private void init() {
        authHeader = new HttpHeaders();
        authHeader.set(clientIdHeader, clientId);
        request = new HttpEntity<>(authHeader);
    }

    @Override
    public Set<UserAnimeTitle> getUserAnimeList(String username) {
        String url = "https://api.myanimelist.net/v2/users/"+username+"/animelist?fields=list_status&status=completed&limit=1000";
        List<Data> dataList = new ArrayList<>();
        while (true) {
            ResponseEntity<GetUserListJsonResult> response = this.restTemplate.exchange(url, HttpMethod.GET, request, GetUserListJsonResult.class);
            dataList.addAll(response.getBody().data());
            if (!response.getBody().paging().containsKey("next"))
                break;
            url = response.getBody().paging().get("next");
        }
        Set<UserAnimeTitle> titleSet = new HashSet<>();
        dataList.forEach(data ->
        {
            int score = data.listStatus().score() != 0 ? data.listStatus().score() : 1;
            titleSet.add(new UserAnimeTitle(AnimeTitle.retrieveFromMalNode(data.node()), score));
        });
        return Set.copyOf(titleSet);
    }

    @Override
    public Set<AnimeRecommendation> getSimilarAnimeTitles(Set<UserAnimeTitle> animeTitles) {
        TitleMapper titleMapper = new TitleMapper();
        titleMapper.fillToExclude(animeTitles);
        CompletableFuture<Void> future = CompletableFuture.allOf();
        for (UserAnimeTitle title : animeTitles) {
            future = CompletableFuture.allOf(future,
                    CompletableFuture.runAsync(() -> {
                        try {
                            titleMapper.findAndAddTitleRecommendations(title);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }));
        }
        future.join();
        Map<AnimeTitle, Integer> result = new HashMap<>(titleMapper.recommendedAnime);
        Set<AnimeRecommendation> animeRecommendationSet = new HashSet<>();
        result.forEach(
                (key, value) -> animeRecommendationSet.add(new AnimeRecommendation(key, value))
        );
        return Set.copyOf(animeRecommendationSet);
    }

    private class TitleMapper {
        private final Map<AnimeTitle, Integer> recommendedAnime = new ConcurrentHashMap<>();
        private Set<AnimeTitle> toExclude = Set.of();
        DelayQueue<DelayedResponse> delayQueue = new DelayQueue<>();
        private long startTime;

        private void fillToExclude(Set<UserAnimeTitle> animeTitles) {
            toExclude = new HashSet<>();
            for (UserAnimeTitle title : animeTitles) {
                toExclude.add(title.animeTitle());
            }
            toExclude = Set.copyOf(toExclude);
        }

        private void findAndAddTitleRecommendations(UserAnimeTitle title) throws InterruptedException {
            String url = "https://api.myanimelist.net/v2/anime/"+title.animeTitle().id()+"?fields=recommendations";
            delayQueue.add(new DelayedResponse(url));
            ResponseEntity<GetAnimeDetail> response = restTemplate.exchange(delayQueue.take().url, HttpMethod.GET, request, GetAnimeDetail.class);
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
}

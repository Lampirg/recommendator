package com.lampirg.recommendator.anidb.mal;

import com.lampirg.recommendator.anidb.AnimeSiteCommunicator;
import com.lampirg.recommendator.anidb.mal.json.Data;
import com.lampirg.recommendator.anidb.mal.json.Recommendation;
import com.lampirg.recommendator.anidb.mal.json.queries.GetAnimeDetail;
import com.lampirg.recommendator.anidb.mal.json.queries.GetUserListJsonResult;
import com.lampirg.recommendator.anidb.mal.titlemapper.TitleMapper;
import com.lampirg.recommendator.model.AnimeRecommendation;
import com.lampirg.recommendator.model.AnimeTitle;
import com.lampirg.recommendator.model.UserAnimeTitle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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


    private RestTemplate restTemplate;

    private TitleMapper titleMapper;

    @Value("${clientIdHeader}")
    private String clientIdHeader;
    @Value("${clientId}")
    private String clientId;
    private HttpHeaders authHeader;
    HttpEntity<String> request;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Autowired
    public void setTitleMapper(@Qualifier("concurrent") TitleMapper titleMapper) {
        this.titleMapper = titleMapper;
    }

    @PostConstruct
    private void init() {
        authHeader = new HttpHeaders();
        authHeader.set(clientIdHeader, clientId);
        request = new HttpEntity<>(authHeader);
    }

    @Override
    public Set<AnimeRecommendation> getSimilarAnimeTitles(String username) {
        return getSimilarAnimeTitles(getUserAnimeList(username));
    }

    public Set<UserAnimeTitle> getUserAnimeList(String username) {
        String url = "https://api.myanimelist.net/v2/users/"+username+"/animelist?fields=list_status&status=completed&limit=1000";
        List<Data> dataList = new ArrayList<>();
        while (true) {
            ResponseEntity<GetUserListJsonResult> response = this.restTemplate.exchange(url, HttpMethod.GET, request, GetUserListJsonResult.class);
            dataList.addAll(Objects.requireNonNull(response.getBody()).data());
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

    public Set<AnimeRecommendation> getSimilarAnimeTitles(Set<UserAnimeTitle> animeTitles) {
        titleMapper.setRequest(request);
        titleMapper.fillToExclude(animeTitles);
        CompletableFuture<Void> future = CompletableFuture.allOf();
        for (UserAnimeTitle title : animeTitles) {
            future = CompletableFuture.allOf(future,
                    CompletableFuture.runAsync(() -> titleMapper.findAndAddTitleRecommendations(title)));
        }
        future.join();
        Map<AnimeTitle, Integer> result = new HashMap<>(titleMapper.getRecommendedAnimeMap());
        Set<AnimeRecommendation> animeRecommendationSet = new HashSet<>();
        result.forEach(
                (key, value) -> animeRecommendationSet.add(new AnimeRecommendation(key, value))
        );
        return Set.copyOf(animeRecommendationSet);
    }
}

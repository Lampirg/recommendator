package com.lampirg.recommendator.anidb;

import com.lampirg.recommendator.mal.Data;
import com.lampirg.recommendator.mal.Recommendation;
import com.lampirg.recommendator.mal.queries.GetAnimeDetail;
import com.lampirg.recommendator.mal.queries.GetUserListJsonResult;
import com.lampirg.recommendator.mal.Node;
import com.lampirg.recommendator.model.AnimeRecommendation;
import com.lampirg.recommendator.model.AnimeTitle;
import com.lampirg.recommendator.model.UserAnimeTitle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MalCommunicator implements AnimeSiteCommunicator {

    @Autowired
    private RestTemplate restTemplate;

    private static final String CLIENT_ID_HEADER = "X-MAL-CLIENT-ID";
    private final String clientId = "a4e33f4b0a5b5e9cbdbbcee74debbb3f";
    private HttpHeaders authHeader;

    @PostConstruct
    private void init() {
        authHeader = new HttpHeaders();
        authHeader.set(CLIENT_ID_HEADER, clientId);
    }

    @Override
    public Set<UserAnimeTitle> getUserAnimeList(String username) {
        String url = "https://api.myanimelist.net/v2/users/"+username+"/animelist?fields=list_status&status=completed&limit=1000";
        HttpEntity<String> request = new HttpEntity<>(authHeader);
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
            Node node = data.node();
            int score = data.listStatus().score() != 0 ? data.listStatus().score() : 1;
            titleSet.add(new UserAnimeTitle(new AnimeTitle(
                    node.id(), node.title(), node.mainPicture().getLargeIfPresent()
            ),
                    score));
        });
        return Set.copyOf(titleSet);
    }

    @Override
    public Set<AnimeRecommendation> getSimilarAnimeTitles(Set<UserAnimeTitle> animeTitles) {
        HttpEntity<String> request = new HttpEntity<>(authHeader);
        Map<AnimeTitle, Integer> recommendedAnime = new HashMap<>();
        Set<AnimeTitle> toExclude = new HashSet<>();
        for (UserAnimeTitle title : animeTitles) {
            toExclude.add(title.animeTitle());
        }
        for (UserAnimeTitle title : animeTitles) {
            String url = "https://api.myanimelist.net/v2/anime/"+title.animeTitle().id()+"?fields=recommendations";
            ResponseEntity<GetAnimeDetail> response = this.restTemplate.exchange(url, HttpMethod.GET, request, GetAnimeDetail.class);
            for (Recommendation recommendation : response.getBody().recommendations()) {
                AnimeTitle animeTitle = new AnimeTitle(
                        recommendation.node().id(),
                        recommendation.node().title(),
                        recommendation.node().mainPicture().getLargeIfPresent()
                );
                if (toExclude.contains(animeTitle))
                    continue;
                recommendedAnime.merge(animeTitle, title.score(), (prev, cur) -> ++prev * title.score());
            }
        }
        Set<AnimeRecommendation> animeRecommendationSet = new HashSet<>();
        recommendedAnime.forEach((key, value) -> animeRecommendationSet.add(new AnimeRecommendation(key, value)));
        return Set.copyOf(animeRecommendationSet);
    }
}

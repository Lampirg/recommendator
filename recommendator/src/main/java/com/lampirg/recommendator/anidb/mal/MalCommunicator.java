package com.lampirg.recommendator.anidb.mal;

import com.lampirg.recommendator.anidb.AnimeSiteCommunicator;
import com.lampirg.recommendator.anidb.mal.json.Data;
import com.lampirg.recommendator.anidb.mal.json.queries.GetUserListJsonResult;
import com.lampirg.recommendator.anidb.mal.titlemapper.TitleMapper;
import com.lampirg.recommendator.model.AnimeRecommendation;
import com.lampirg.recommendator.model.AnimeTitle;
import com.lampirg.recommendator.model.UserAnimeTitle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
@PropertySource("classpath:mal security code.yml")
public class MalCommunicator implements AnimeSiteCommunicator {

    private RestTemplate restTemplate;
    private TitleMapper titleMapper;

    @Value("${clientIdHeader}")
    private String clientIdHeader;
    @Value("${clientId}")
    private String clientId;
    HttpEntity<String> request;

    private final static int LIMIT_SIZE = 50;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Autowired
    public void setTitleMapper(@Qualifier("singleThread") TitleMapper titleMapper) {
        this.titleMapper = titleMapper;
    }

    @PostConstruct
    private void init() {
        HttpHeaders authHeader = new HttpHeaders();
        authHeader.set(clientIdHeader, clientId);
        request = new HttpEntity<>(authHeader);
    }

    @Override
    public Set<AnimeRecommendation> getSimilarAnimeTitles(String username) {
        // TODO: QueryMaker interface/classes
        Set<UserAnimeTitle> completed = getUserCompletedAnimeList(username);
        Set<UserAnimeTitle> watching = getUserWatchingAnimeList(username);
        Set<UserAnimeTitle> dropped = getUserDroppedAnimeList(username);
        Set<UserAnimeTitle> onHold = getUserOnHoldAnimeList(username);
        Set<UserAnimeTitle> toExclude = Stream.of(completed, watching, dropped, onHold)
                .flatMap(Set::stream).collect(Collectors.toSet());
        Set<UserAnimeTitle> toInclude = completed.stream()
                .sorted(
                        Comparator.comparingInt(UserAnimeTitle::score).reversed()
                                .thenComparing(x -> x.animeTitle().name())
                )
                .limit(LIMIT_SIZE).collect(Collectors.toSet());
        return getSimilarAnimeTitles(toInclude, toExclude);
    }

    public Set<UserAnimeTitle> getUserCompletedAnimeList(String username) {
        return getUserAnimeList(username, "completed");
    }
    public Set<UserAnimeTitle> getUserWatchingAnimeList(String username) {
        return getUserAnimeList(username, "watching");
    }
    public Set<UserAnimeTitle> getUserDroppedAnimeList(String username) {
        return getUserAnimeList(username, "dropped");
    }
    public Set<UserAnimeTitle> getUserOnHoldAnimeList(String username) {
        return getUserAnimeList(username, "on_hold");
    }

    public Set<UserAnimeTitle> getUserAnimeList(String username, String listType) {
        String url = "https://api.myanimelist.net/v2/users/"+username+"/animelist?fields=list_status&status="+listType+"&limit=1000";
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

    public Set<AnimeRecommendation> getSimilarAnimeTitles(Set<UserAnimeTitle> animeTitles, Set<UserAnimeTitle> toExclude) {
        Map<AnimeTitle, Integer> result = titleMapper.setRequest(request).fillToExclude(toExclude)
                .getRecommendedAnimeMap(animeTitles);
        Set<AnimeRecommendation> animeRecommendationSet = new HashSet<>();
        result.forEach((key, value) -> animeRecommendationSet.add(new AnimeRecommendation(key, value)));
        return Set.copyOf(animeRecommendationSet);
    }
}

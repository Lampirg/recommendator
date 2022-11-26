package com.lampirg.recommendator.anidb.mal.querymaker;

import com.lampirg.recommendator.anidb.mal.json.Data;
import com.lampirg.recommendator.anidb.mal.json.queries.GetUserListJsonResult;
import com.lampirg.recommendator.anidb.model.AnimeTitle;
import com.lampirg.recommendator.anidb.model.UserAnimeTitle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Qualifier("single")
@Scope("prototype")
public class SingleThreadQueryMaker implements QueryMaker {

    RestTemplate restTemplate;
    private HttpEntity<String> request;

    private String username;
    private Set<UserAnimeTitle> completed;
    private Set<UserAnimeTitle> watching;
    private Set<UserAnimeTitle> dropped;
    private Set<UserAnimeTitle> onHold;

    private final static int LIMIT_SIZE = 50;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public QueryMaker setRequest(HttpEntity<String> request) {
        this.request = request;
        return this;
    }

    @Override
    public void setUser(String username) {
        this.username = username;
        completed = getUserCompletedAnimeList(username);
        watching = getUserWatchingAnimeList(username);
        dropped = getUserDroppedAnimeList(username);
        onHold = getUserOnHoldAnimeList(username);
    }

    @Override
    public Set<UserAnimeTitle> getToExclude() {
        return Stream.of(completed, watching, dropped, onHold)
                .flatMap(Set::stream).collect(Collectors.toSet());
    }

    @Override
    public Set<UserAnimeTitle> getToInclude() {
        return completed.stream()
                .sorted(
                        Comparator.comparingInt(UserAnimeTitle::score).reversed()
                                .thenComparing(x -> x.animeTitle().name())
                )
                .limit(LIMIT_SIZE).collect(Collectors.toSet());
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
}

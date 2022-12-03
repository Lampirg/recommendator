package com.lampirg.recommendator.anidb.shikimori;


import com.lampirg.recommendator.anidb.UserListExtractor;
import com.lampirg.recommendator.anidb.model.AnimeTitle;
import com.lampirg.recommendator.anidb.model.UserAnimeTitle;
import com.lampirg.recommendator.anidb.shikimori.json.ShikiNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Scope("prototype")
@Qualifier("shiki")
public class ShikimoriListExtractor implements UserListExtractor {
    ShikimoriQueryMaker queryMaker;
    private HttpEntity<String> request;

    protected String username;
    protected Set<UserAnimeTitle> completed;
    protected Set<UserAnimeTitle> watching;
    protected Set<UserAnimeTitle> dropped;
    protected Set<UserAnimeTitle> onHold;

    private final static int LIMIT_SIZE = 50;

    @Autowired
    public void setQueryMaker(ShikimoriQueryMaker queryMaker) {
        this.queryMaker = queryMaker;
    }

    @Override
    public UserListExtractor setRequest(HttpEntity<String> request) {
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
        List<ShikiNode> dataList = new ArrayList<>();
        int page = 1;
        int limit = 100;
        while (true) {
            String url = "https://shikimori.one/api/users/"+username+"/anime_rates?status="+listType+"&censored=false&limit="+limit+"&page="+page;
            ResponseEntity<List<ShikiNode>> response = queryMaker.exchange(
                    url,
                    HttpMethod.GET,
                    request,
                    new ParameterizedTypeReference<List<ShikiNode>>() {}
            );
            dataList.addAll(Objects.requireNonNull(response.getBody()));
            if (response.getBody().size() != limit + 1)
                break;
            page++;
        }
        Set<UserAnimeTitle> titleSet = new HashSet<>();
        dataList.forEach(data ->
        {
            int score = data.score() != 0 ? data.score() : 1;
            titleSet.add(new UserAnimeTitle(AnimeTitle.retrieveFromShikiNode(data), score));
        });
        return Set.copyOf(titleSet);
    }
}

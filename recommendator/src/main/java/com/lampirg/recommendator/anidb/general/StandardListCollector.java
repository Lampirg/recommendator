package com.lampirg.recommendator.anidb.general;

import com.lampirg.recommendator.anidb.general.model.UserAnimeTitle;
import org.springframework.http.HttpEntity;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class StandardListCollector implements UserListExtractor {

    private HttpEntity<String> request;

    protected String username;
    protected Set<UserAnimeTitle> completed;
    protected Set<UserAnimeTitle> watching;
    protected Set<UserAnimeTitle> dropped;
    protected Set<UserAnimeTitle> onHold;

    private final static int LIMIT_SIZE = 50;

    protected final HttpEntity<String> getRequest() {
        return request;
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

    protected Set<UserAnimeTitle> getUserCompletedAnimeList(String username) {
        return getUserAnimeList(username, "completed");
    }
    protected Set<UserAnimeTitle> getUserWatchingAnimeList(String username) {
        return getUserAnimeList(username, "watching");
    }
    protected Set<UserAnimeTitle> getUserDroppedAnimeList(String username) {
        return getUserAnimeList(username, "dropped");
    }
    protected Set<UserAnimeTitle> getUserOnHoldAnimeList(String username) {
        return getUserAnimeList(username, "on_hold");
    }

    public abstract Set<UserAnimeTitle> getUserAnimeList(String username, String listType);
}
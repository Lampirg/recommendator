package com.lampirg.recommendator.anidb.general.listextractor;

import com.lampirg.recommendator.anidb.titles.model.UserAnimeTitle;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class StandardListExtractor implements UserListExtractor {

    private Map<ListType, Set<UserAnimeTitle>> userAnimeLists;

    private final static int LIMIT_SIZE = 50;

    @Override
    public Set<UserAnimeTitle> getToExclude(String username) {
        initializeUserIfEmpty(username);
        return userAnimeLists.values().stream()
                .flatMap(Set::stream).collect(Collectors.toSet());
    }

    @Override
    public Set<UserAnimeTitle> getToInclude(String username) {
        initializeUserIfEmpty(username);
        return userAnimeLists.get(ListType.COMPLETED).stream()
                .sorted(
                        Comparator.comparingInt(UserAnimeTitle::score).reversed()
                                .thenComparing(x -> x.animeTitle().name())
                )
                .limit(LIMIT_SIZE).collect(Collectors.toSet());
    }

    private void initializeUserIfEmpty(String username) {
        if (userAnimeLists != null)
            return;
        userAnimeLists = new HashMap<>(4);
        userAnimeLists.put(ListType.COMPLETED, getUserCompletedAnimeList(username));
        userAnimeLists.put(ListType.WATCHING, getUserWatchingAnimeList(username));
        userAnimeLists.put(ListType.DROPPED, getUserDroppedAnimeList(username));
        userAnimeLists.put(ListType.ON_HOLD, getUserOnHoldAnimeList(username));
    }

    public final Set<UserAnimeTitle> getUserCompletedAnimeList(String username) {
        return getUserAnimeList(username, ListType.COMPLETED);
    }
    public final Set<UserAnimeTitle> getUserWatchingAnimeList(String username) {
        return getUserAnimeList(username, ListType.WATCHING);
    }
    public final Set<UserAnimeTitle> getUserDroppedAnimeList(String username) {
        return getUserAnimeList(username, ListType.DROPPED);
    }
    public final Set<UserAnimeTitle> getUserOnHoldAnimeList(String username) {
        return getUserAnimeList(username, ListType.ON_HOLD);
    }

    protected abstract Set<UserAnimeTitle> getUserAnimeList(String username, ListType listType);
}

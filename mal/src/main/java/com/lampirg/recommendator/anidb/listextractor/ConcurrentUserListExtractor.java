package com.lampirg.recommendator.anidb.listextractor;

import com.lampirg.recommendator.anidb.general.listextractor.UserListExtractor;

import java.util.concurrent.CompletableFuture;

public class ConcurrentUserListExtractor extends AbstractMalUserListExtractor implements UserListExtractor {
    @Override
    public void setUser(String username) {
        this.username = username;
        CompletableFuture<Void> future =
                CompletableFuture.runAsync(() -> completed = getUserCompletedAnimeList(username))
                        .thenRunAsync(() -> watching = getUserWatchingAnimeList(username))
                        .thenRunAsync(() -> dropped = getUserDroppedAnimeList(username))
                        .thenRunAsync(() -> onHold = getUserOnHoldAnimeList(username));
        future.join();
    }
}

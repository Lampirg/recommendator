package com.lampirg.recommendator.anidb.mal.querymaker;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@Qualifier("concurrent")
@Scope("prototype")
public class ConcurrentQueryMaker extends AbstractQueryMaker implements QueryMaker {
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

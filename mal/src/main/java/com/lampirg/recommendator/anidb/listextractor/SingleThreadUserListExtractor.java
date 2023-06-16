package com.lampirg.recommendator.anidb.listextractor;

import com.lampirg.recommendator.anidb.general.listextractor.UserListExtractor;


public class SingleThreadUserListExtractor extends AbstractMalUserListExtractor implements UserListExtractor {
    @Override
    public void setUser(String username) {
        this.username = username;
        completed = getUserCompletedAnimeList(username);
        watching = getUserWatchingAnimeList(username);
        dropped = getUserDroppedAnimeList(username);
        onHold = getUserOnHoldAnimeList(username);
    }

}

package com.lampirg.recommendator.anidb.mal.listextractor;

import com.lampirg.recommendator.anidb.UserListExtractor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Qualifier("single")
@Scope("prototype")
public class SingleThreadUserListExtractor extends AbstractUserListExtractor implements UserListExtractor {
    @Override
    public void setUser(String username) {
        this.username = username;
        completed = getUserCompletedAnimeList(username);
        watching = getUserWatchingAnimeList(username);
        dropped = getUserDroppedAnimeList(username);
        onHold = getUserOnHoldAnimeList(username);
    }

}

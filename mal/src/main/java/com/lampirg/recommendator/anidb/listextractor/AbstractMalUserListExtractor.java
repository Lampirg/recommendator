package com.lampirg.recommendator.anidb.listextractor;

import com.lampirg.recommendator.anidb.Utils;
import com.lampirg.recommendator.anidb.general.listextractor.StandardListExtractor;
import com.lampirg.recommendator.anidb.general.listextractor.UserListExtractor;
import com.lampirg.recommendator.anidb.json.Data;
import com.lampirg.recommendator.anidb.json.ListStatus;
import com.lampirg.recommendator.anidb.query.MalUserListFinder;
import com.lampirg.recommendator.anidb.titles.model.UserAnimeTitle;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractMalUserListExtractor extends StandardListExtractor implements UserListExtractor {

    private MalUserListFinder userListFinder;

    @Autowired
    public void setUserListFinder(MalUserListFinder userListFinder) {
        this.userListFinder = userListFinder;
    }

    @Override
    public abstract void setUser(String username);

    public Set<UserAnimeTitle> getUserAnimeList(String username, String listType) {
        return userListFinder.findUserList(username, listType).stream()
                .map(AbstractMalUserListExtractor::adjustDataWithScoreEqualToZero)
                .map(data -> new UserAnimeTitle(
                        Utils.retrieveFromMalNode(data.node()),
                        data.listStatus().score()
                ))
                .collect(Collectors.toSet());
    }

    private static Data adjustDataWithScoreEqualToZero(Data data) {
        if (data.listStatus().score() != 0)
            return data;
        return new Data(data.node(), new ListStatus(1));
    }
}

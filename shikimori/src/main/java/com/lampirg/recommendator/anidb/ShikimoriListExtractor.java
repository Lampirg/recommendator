package com.lampirg.recommendator.anidb;


import com.lampirg.recommendator.anidb.general.listextractor.ListType;
import com.lampirg.recommendator.anidb.general.listextractor.StandardListExtractor;
import com.lampirg.recommendator.anidb.general.listextractor.UserListExtractor;
import com.lampirg.recommendator.anidb.json.ShikiUserNode;
import com.lampirg.recommendator.anidb.query.ShikimoriQueryMaker;
import com.lampirg.recommendator.anidb.query.ShikimoriUserListFinder;
import com.lampirg.recommendator.anidb.titles.model.UserAnimeTitle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.*;
import java.util.stream.Collectors;


public class ShikimoriListExtractor extends StandardListExtractor implements UserListExtractor {

    private ShikimoriUserListFinder userListFinder;

    @Autowired
    public void setUserListFinder(ShikimoriUserListFinder userListFinder) {
        this.userListFinder = userListFinder;
    }

    @Override
    public Set<UserAnimeTitle> getUserAnimeList(String username, ListType listType) {
        return userListFinder.findUserList(username, listType.getDefaultValue()).stream()
                .map(this::adjustDataWithScoreEqualToZero)
                .map(shikiUserNode -> new UserAnimeTitle(
                        Utils.retrieveFromShikiNode(shikiUserNode.anime()),
                        shikiUserNode.score()
                ))
                .collect(Collectors.toUnmodifiableSet());
    }

    private ShikiUserNode adjustDataWithScoreEqualToZero(ShikiUserNode shikiUserNode) {
        if (shikiUserNode.score() != 0)
            return shikiUserNode;
        return new ShikiUserNode(1, shikiUserNode.anime());
    }
}

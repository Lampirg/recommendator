package com.lampirg.recommendator.anidb.specific.mal.listextractor;

import com.lampirg.recommendator.anidb.general.listextractor.StandardListExtractor;
import com.lampirg.recommendator.anidb.general.listextractor.UserListExtractor;
import com.lampirg.recommendator.anidb.specific.mal.MalQueryMaker;
import com.lampirg.recommendator.anidb.specific.mal.json.Data;
import com.lampirg.recommendator.anidb.specific.mal.json.queries.GetUserListJsonResult;
import com.lampirg.recommendator.anidb.titles.model.AnimeTitle;
import com.lampirg.recommendator.anidb.titles.model.UserAnimeTitle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.*;

public abstract class AbstractMalUserListExtractor extends StandardListExtractor implements UserListExtractor {

    private MalQueryMaker queryMaker;

    @Autowired
    public void setQueryMaker(MalQueryMaker queryMaker) {
        this.queryMaker = queryMaker;
    }

    @Override
    public abstract void setUser(String username);

    public Set<UserAnimeTitle> getUserAnimeList(String username, String listType) {
        String url = "https://api.myanimelist.net/v2/users/"+username+"/animelist?fields=list_status&status="+listType+"&limit=1000";
        List<Data> dataList = new ArrayList<>();
        while (true) {
            ResponseEntity<GetUserListJsonResult> response = this.queryMaker.exchange(
                    url,
                    HttpMethod.GET,
                    GetUserListJsonResult.class
            );
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

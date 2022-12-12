package com.lampirg.recommendator.anidb.specific.shikimori;


import com.lampirg.recommendator.anidb.general.StandardListCollector;
import com.lampirg.recommendator.anidb.general.UserListExtractor;
import com.lampirg.recommendator.anidb.general.model.AnimeTitle;
import com.lampirg.recommendator.anidb.general.model.UserAnimeTitle;
import com.lampirg.recommendator.anidb.specific.shikimori.json.ShikiUserNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.*;


public class ShikimoriListExtractor extends StandardListCollector implements UserListExtractor {

    private ShikimoriQueryMaker queryMaker;

    @Autowired
    public void setQueryMaker(ShikimoriQueryMaker queryMaker) {
        this.queryMaker = queryMaker;
    }

    public Set<UserAnimeTitle> getUserAnimeList(String username, String listType) {
        List<ShikiUserNode> dataList = new ArrayList<>();
        int page = 1;
        int limit = 100;
        while (true) {
            String url = "https://shikimori.one/api/users/"+username+"/anime_rates?status="+listType+"&censored=false&limit="+limit+"&page="+page;
            ResponseEntity<List<ShikiUserNode>> response = queryMaker.exchange(
                    url,
                    HttpMethod.GET,
                    getRequest(),
                    new ParameterizedTypeReference<>() {}
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
            titleSet.add(new UserAnimeTitle(AnimeTitle.retrieveFromShikiNode(data.anime()), score));
        });
        return Set.copyOf(titleSet);
    }
}

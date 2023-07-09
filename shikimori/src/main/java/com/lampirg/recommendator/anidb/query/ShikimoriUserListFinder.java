package com.lampirg.recommendator.anidb.query;

import com.lampirg.recommendator.anidb.json.ShikiUserNode;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ShikimoriUserListFinder {
    private ShikimoriQueryMaker queryMaker;

    public ShikimoriUserListFinder(ShikimoriQueryMaker queryMaker) {
        this.queryMaker = queryMaker;
    }

    public List<ShikiUserNode> findUserList(String username, String listType) {
        List<ShikiUserNode> dataList = new ArrayList<>();
        int page = 1;
        int limit = 100;
        while (true) {
            String queryUrl = "https://shikimori.one/api/users/"+username+"/anime_rates?status="+listType+"&censored=false&limit="+limit+"&page="+page;
            ResponseEntity<List<ShikiUserNode>> response = queryMaker.exchange(
                    queryUrl,
                    HttpMethod.GET,
                    new ParameterizedTypeReference<>() {}
            );
            dataList.addAll(Objects.requireNonNull(response.getBody()));
            if (response.getBody().size() != limit + 1)
                break;
            page++;
        }
        return dataList;
    }
}

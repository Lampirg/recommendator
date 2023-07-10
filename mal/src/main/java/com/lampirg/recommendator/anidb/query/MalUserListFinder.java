package com.lampirg.recommendator.anidb.query;

import com.lampirg.recommendator.anidb.json.Data;
import com.lampirg.recommendator.anidb.json.queries.GetUserListJsonResult;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class MalUserListFinder {

    private MalQueryMaker queryMaker;

    public MalUserListFinder(MalQueryMaker queryMaker) {
        this.queryMaker = queryMaker;
    }

    public List<Data> findUserList(String username, String listType) {
        Optional<String> queryUrl = Optional.of(
                "https://api.myanimelist.net/v2/users/" + username + "/animelist?fields=list_status&status=" + listType + "&limit=1000"
        );
        List<Data> dataList = new ArrayList<>();
        Tuple tuple = new Tuple(queryUrl, dataList);
        while (tuple.queryUrl.isPresent()) {
            tuple = performQuery(tuple.queryUrl);
            dataList.addAll(tuple.dataList);
        }
        return List.copyOf(dataList);
    }

    private Tuple performQuery(Optional<String> queryUrl) {
        ResponseEntity<GetUserListJsonResult> response = this.queryMaker.exchange(
                queryUrl.get(),
                HttpMethod.GET,
                GetUserListJsonResult.class
        );
        queryUrl = Optional.ofNullable(response.getBody().paging().get("next"));
        return new Tuple(queryUrl, response.getBody().data());
    }
    record Tuple(Optional<String> queryUrl, List<Data> dataList) {}
}

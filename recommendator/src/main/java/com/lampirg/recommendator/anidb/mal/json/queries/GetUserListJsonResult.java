package com.lampirg.recommendator.anidb.mal.json.queries;


import com.lampirg.recommendator.anidb.mal.json.Data;

import java.util.List;
import java.util.Map;

public record GetUserListJsonResult(
        List<Data> data,
        Map<String, String> paging
) {
}

package com.lampirg.recommendator.mal.queries;


import com.lampirg.recommendator.mal.Data;

import java.util.List;
import java.util.Map;

public record GetUserListJsonResult(
        List<Data> data,
        Map<String, String> paging
) {
}

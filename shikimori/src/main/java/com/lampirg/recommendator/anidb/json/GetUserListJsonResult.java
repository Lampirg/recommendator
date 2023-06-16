package com.lampirg.recommendator.anidb.json;

import java.util.List;

public record GetUserListJsonResult(
        List<ShikiUserNode> data
) {
}

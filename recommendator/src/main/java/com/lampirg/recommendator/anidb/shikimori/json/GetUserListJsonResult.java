package com.lampirg.recommendator.anidb.shikimori.json;

import java.util.List;

public record GetUserListJsonResult(
        List<ShikiNode> data
) {
}

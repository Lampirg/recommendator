package com.lampirg.recommendator.anidb.specific.shikimori.json;

import java.util.List;

public record GetUserListJsonResult(
        List<ShikiUserNode> data
) {
}

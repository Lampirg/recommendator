package com.lampirg.recommendator.anidb.shikimori.json;

public record ShikiUserNode(
        int score,
        String name,
        ShikiNode anime
) {
}

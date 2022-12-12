package com.lampirg.recommendator.anidb.specific.shikimori.json;

public record ShikiUserNode(
        int score,
        String name,
        ShikiNode anime
) {
}

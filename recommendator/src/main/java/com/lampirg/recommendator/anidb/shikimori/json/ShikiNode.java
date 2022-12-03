package com.lampirg.recommendator.anidb.shikimori.json;

public record ShikiNode(
        int score,
        String name,
        AnimeNode anime
) {
}

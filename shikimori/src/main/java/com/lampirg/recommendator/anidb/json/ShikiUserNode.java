package com.lampirg.recommendator.anidb.json;

public record ShikiUserNode(
        int score,
        String name,
        ShikiNode anime
) {
}

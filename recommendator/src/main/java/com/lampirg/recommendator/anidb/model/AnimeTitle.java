package com.lampirg.recommendator.anidb.model;

import com.lampirg.recommendator.anidb.mal.json.MalNode;
import com.lampirg.recommendator.anidb.shikimori.json.ShikiUserNode;

public record AnimeTitle(
        long id,
        String name,
        String imageUrl
) {
    public static AnimeTitle retrieveFromMalNode(MalNode node) {
        return new AnimeTitle(node.id(), node.title(), node.mainPicture().getLargeIfPresent());
    }

    public static AnimeTitle retrieveFromShikiNode(ShikiUserNode node) {
        return new AnimeTitle(node.anime().id(), node.anime().name(), node.anime().image().original());
    }
}

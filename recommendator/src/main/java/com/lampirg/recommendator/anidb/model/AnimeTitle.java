package com.lampirg.recommendator.anidb.model;

import com.lampirg.recommendator.anidb.mal.json.MalNode;
import com.lampirg.recommendator.anidb.shikimori.json.ShikiNode;
import com.lampirg.recommendator.anidb.shikimori.json.ShikiUserNode;

public record AnimeTitle(
        long id,
        String name,
        String imageUrl
) {
    public static AnimeTitle retrieveFromMalNode(MalNode node) {
        return new AnimeTitle(node.id(), node.title(), node.mainPicture().getLargeIfPresent());
    }

    public static AnimeTitle retrieveFromShikiNode(ShikiNode node) {
        return new AnimeTitle(node.id(), node.name(), "https://shikimori.one"+node.image().original());
    }
}

package com.lampirg.recommendator.anidb.general.model;

import com.lampirg.recommendator.anidb.mal.json.MalNode;
import com.lampirg.recommendator.anidb.shikimori.json.ShikiNode;

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

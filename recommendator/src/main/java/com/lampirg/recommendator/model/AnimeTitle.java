package com.lampirg.recommendator.model;

import com.lampirg.recommendator.anidb.mal.json.Node;

public record AnimeTitle(
        long id,
        String name,
        String imageUrl
) {
    public static AnimeTitle retrieveFromMalNode(Node node) {
        return new AnimeTitle(node.id(), node.title(), node.mainPicture().getLargeIfPresent());
    }
}

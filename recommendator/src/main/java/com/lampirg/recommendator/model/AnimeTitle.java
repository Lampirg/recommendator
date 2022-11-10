package com.lampirg.recommendator.model;

import com.lampirg.recommendator.mal.Node;

public record AnimeTitle(
        long id,
        String name,
        String imageUrl
) {
    public static AnimeTitle retreiveFromMalNode(Node node) {
        return new AnimeTitle(node.id(), node.title(), node.mainPicture().getLargeIfPresent());
    }
}

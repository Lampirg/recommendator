package com.lampirg.recommendator.anidb.titles.model;

import com.lampirg.recommendator.anidb.specific.anilist.json.Completed;
import com.lampirg.recommendator.anidb.specific.anilist.json.Other;
import com.lampirg.recommendator.anidb.specific.mal.json.MalNode;
import com.lampirg.recommendator.anidb.specific.shikimori.json.ShikiNode;

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

    public static AnimeTitle retrieveFromAnilistMedia(Other.Lists.Entries.Media media) {
        return new AnimeTitle(media.id(), media.title().romaji(), media.coverImage().extraLarge().replaceAll("\\\\", ""));
    }
    public static AnimeTitle retrieveFromAnilistMedia(Completed.CompletedList.Entries.Media media) {
        return new AnimeTitle(media.id(), media.title().romaji(), media.coverImage().extraLarge().replaceAll("\\\\", ""));
    }
    public static AnimeTitle retrieveFromAnilistMedia(Completed.CompletedList.Entries.Media.Recommendations.Nodes.MediaRecommendation media) {
        return new AnimeTitle(media.id(), media.title().romaji(), media.coverImage().extraLarge().replaceAll("\\\\", ""));
    }
}

package com.lampirg.recommendator.anidb;

import com.lampirg.recommendator.anidb.json.ShikiNode;
import com.lampirg.recommendator.anidb.titles.model.AnimeTitle;

public class Utils {
    public static AnimeTitle retrieveFromShikiNode(ShikiNode node) {
        return new AnimeTitle(node.id(), node.name(), "https://shikimori.one"+node.image().original());
    }
}

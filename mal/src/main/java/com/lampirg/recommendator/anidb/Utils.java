package com.lampirg.recommendator.anidb;

import com.lampirg.recommendator.anidb.json.MalNode;
import com.lampirg.recommendator.anidb.titles.model.AnimeTitle;

public class Utils {
    public static AnimeTitle retrieveFromMalNode(MalNode node) {
        return new AnimeTitle(node.id(), node.title(), node.mainPicture().getLargeIfPresent());
    }
}

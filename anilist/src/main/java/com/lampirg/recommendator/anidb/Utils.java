package com.lampirg.recommendator.anidb;

import com.lampirg.recommendator.anidb.json.Completed;
import com.lampirg.recommendator.anidb.json.Other;
import com.lampirg.recommendator.anidb.titles.model.AnimeTitle;

public class Utils {
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

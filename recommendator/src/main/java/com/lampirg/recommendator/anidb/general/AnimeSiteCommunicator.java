package com.lampirg.recommendator.anidb.general;

import com.lampirg.recommendator.anidb.titles.model.AnimeRecommendation;

import java.util.Set;

public interface AnimeSiteCommunicator {
    // TODO: add other sites support (Shikimori, aniDB, etc.)
    Set<AnimeRecommendation> getSimilarAnimeTitles(String username);
}

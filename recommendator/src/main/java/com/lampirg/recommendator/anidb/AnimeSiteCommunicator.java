package com.lampirg.recommendator.anidb;

import com.lampirg.recommendator.anidb.model.AnimeRecommendation;

import java.util.Set;

public interface AnimeSiteCommunicator {
    // TODO: add other sites support (Shikimori, aniDB, etc.)
    Set<AnimeRecommendation> getSimilarAnimeTitles(String username);
}

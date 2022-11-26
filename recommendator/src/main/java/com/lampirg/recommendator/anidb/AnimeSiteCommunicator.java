package com.lampirg.recommendator.anidb;

import com.lampirg.recommendator.anidb.model.AnimeRecommendation;

import java.util.Set;

public interface AnimeSiteCommunicator {
    Set<AnimeRecommendation> getSimilarAnimeTitles(String username);
}

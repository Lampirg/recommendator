package com.lampirg.recommendator.anidb.general;

import com.lampirg.recommendator.anidb.titles.model.AnimeRecommendation;

import java.util.Set;

public interface AnimeSiteCommunicator {
    Set<AnimeRecommendation> getSimilarAnimeTitles(String username);
}

package com.lampirg.recommendator.anidb;

import com.lampirg.recommendator.model.AnimeRecommendation;
import com.lampirg.recommendator.model.AnimeTitle;
import com.lampirg.recommendator.model.UserAnimeTitle;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface AnimeSiteCommunicator {
    Set<UserAnimeTitle> getUserAnimeList(String username);
    Map<AnimeTitle, Integer> getSimilarAnimeTitles(Set<UserAnimeTitle> animeTitles);
}

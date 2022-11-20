package com.lampirg.recommendator.anidb;

import com.lampirg.recommendator.model.AnimeRecommendation;
import com.lampirg.recommendator.model.AnimeTitle;
import com.lampirg.recommendator.model.UserAnimeTitle;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface AnimeSiteCommunicator {
    // TODO: add getters for different types of user lists (like dropped, on-hold, etc.), maybe in sub interface
    Set<UserAnimeTitle> getUserAnimeList(String username);
    Set<AnimeRecommendation> getSimilarAnimeTitles(Set<UserAnimeTitle> animeTitles);
}

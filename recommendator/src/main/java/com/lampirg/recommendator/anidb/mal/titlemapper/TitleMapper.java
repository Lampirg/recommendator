package com.lampirg.recommendator.anidb.mal.titlemapper;

import com.lampirg.recommendator.model.AnimeTitle;
import com.lampirg.recommendator.model.UserAnimeTitle;
import org.springframework.http.HttpEntity;

import java.util.Map;
import java.util.Set;

public interface TitleMapper {
    void setRequest(HttpEntity<String> request);
    void fillToExclude(Set<UserAnimeTitle> animeTitles);

    void findAndAddTitleRecommendations(UserAnimeTitle title);

    Map<AnimeTitle, Integer> getRecommendedAnimeMap();
}

package com.lampirg.recommendator.anidb.general.titlemapper;

import com.lampirg.recommendator.anidb.general.model.AnimeTitle;
import com.lampirg.recommendator.anidb.general.model.UserAnimeTitle;
import org.springframework.http.HttpEntity;

import java.util.Map;
import java.util.Set;

public interface TitleMapper {
    TitleMapper setRequest(HttpEntity<String> request);
    TitleMapper fillToExclude(Set<UserAnimeTitle> toExclude);
    Map<AnimeTitle, Integer> getRecommendedAnimeMap(Set<UserAnimeTitle> animeTitles);
}

package com.lampirg.recommendator.anidb.specific.mal.titlemapper;

import com.lampirg.recommendator.anidb.general.titlemapper.TitleMapper;
import com.lampirg.recommendator.anidb.titles.model.AnimeTitle;
import com.lampirg.recommendator.anidb.titles.model.UserAnimeTitle;

import java.util.*;

public class SingleThreadTitleMapper extends AbstractMalTitleMapper implements TitleMapper {
    @Override
    public Map<AnimeTitle, Integer> getRecommendedAnimeMap(Set<UserAnimeTitle> animeTitles) {
        if (recommendedAnime == null)
            recommendedAnime = new HashMap<>();
        if (!recommendedAnime.isEmpty())
            return recommendedAnime;
        for (UserAnimeTitle title : animeTitles) {
            findAndAddTitleRecommendations(title);
        }
        return recommendedAnime;
    }
}

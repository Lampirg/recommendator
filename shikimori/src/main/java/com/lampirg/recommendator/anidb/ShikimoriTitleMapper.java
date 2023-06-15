package com.lampirg.recommendator.anidb;

import com.lampirg.recommendator.anidb.general.titlemapper.IterativeTitleMapper;
import com.lampirg.recommendator.anidb.general.titlemapper.TitleMapper;
import com.lampirg.recommendator.anidb.titles.model.AnimeTitle;
import com.lampirg.recommendator.anidb.titles.model.UserAnimeTitle;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ShikimoriTitleMapper extends IterativeTitleMapper implements TitleMapper {

    private ShikimoriCacher repository;


    @Autowired
    public void setRepository(ShikimoriCacher repository) {
        this.repository = repository;
    }

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

    @Override
    protected void findAndAddTitleRecommendations(UserAnimeTitle title) {
        Set<AnimeTitle> recommendedTitles = repository.getRecommendations(title.animeTitle());
        for (AnimeTitle animeTitle : recommendedTitles) {
            if (getToExclude().contains(animeTitle))
                continue;
            recommendedAnime.merge(animeTitle, title.score(), Integer::sum);
        }
    }
}

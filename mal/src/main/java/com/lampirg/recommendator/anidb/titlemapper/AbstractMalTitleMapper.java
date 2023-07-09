package com.lampirg.recommendator.anidb.titlemapper;

import com.lampirg.recommendator.anidb.general.titlemapper.IterativeTitleMapper;
import com.lampirg.recommendator.anidb.general.titlemapper.TitleMapper;
import com.lampirg.recommendator.anidb.MalRecommendationsFinder;
import com.lampirg.recommendator.anidb.titles.model.UserAnimeTitle;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractMalTitleMapper extends IterativeTitleMapper implements TitleMapper {

    private MalRecommendationsFinder dataExtractor;

    @Autowired
    public void setDataExtractor(MalRecommendationsFinder dataExtractor) {
        this.dataExtractor = dataExtractor;
    }

    protected final void findAndAddTitleRecommendations(UserAnimeTitle userAnimeTitle) {
        dataExtractor.findRecommendations(userAnimeTitle.animeTitle()).stream()
                .filter(animeTitle -> !getToExclude().contains(animeTitle))
                .forEach(animeTitle -> recommendedAnime.merge(animeTitle, userAnimeTitle.score(), Integer::sum));
    }
}

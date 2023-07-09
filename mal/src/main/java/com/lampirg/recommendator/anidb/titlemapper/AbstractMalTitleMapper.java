package com.lampirg.recommendator.anidb.titlemapper;

import com.lampirg.recommendator.anidb.general.titlemapper.IterativeTitleMapper;
import com.lampirg.recommendator.anidb.general.titlemapper.TitleMapper;
import com.lampirg.recommendator.anidb.MalRecommendationsExtractor;
import com.lampirg.recommendator.anidb.titles.model.AnimeTitle;
import com.lampirg.recommendator.anidb.titles.model.UserAnimeTitle;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public abstract class AbstractMalTitleMapper extends IterativeTitleMapper implements TitleMapper {

    private MalRecommendationsExtractor dataExtractor;

    @Autowired
    public void setDataExtractor(MalRecommendationsExtractor dataExtractor) {
        this.dataExtractor = dataExtractor;
    }

    protected final void findAndAddTitleRecommendations(UserAnimeTitle userAnimeTitle) {
        dataExtractor.findRecommendations(userAnimeTitle.animeTitle()).stream()
                .filter(animeTitle -> !getToExclude().contains(animeTitle))
                .forEach(animeTitle -> recommendedAnime.merge(animeTitle, userAnimeTitle.score(), Integer::sum));
    }
}

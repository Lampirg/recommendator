package com.lampirg.recommendator.anidb.titlemapper;

import com.lampirg.recommendator.anidb.general.titlemapper.IterativeTitleMapper;
import com.lampirg.recommendator.anidb.general.titlemapper.TitleMapper;
import com.lampirg.recommendator.anidb.MalCacher;
import com.lampirg.recommendator.anidb.titles.model.AnimeTitle;
import com.lampirg.recommendator.anidb.titles.model.UserAnimeTitle;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public abstract class AbstractMalTitleMapper extends IterativeTitleMapper implements TitleMapper {

    private MalCacher dataExtractor;

    @Autowired
    public void setDataExtractor(MalCacher dataExtractor) {
        this.dataExtractor = dataExtractor;
    }

    protected final void findAndAddTitleRecommendations(UserAnimeTitle title) {
        Set<AnimeTitle> recommendedTitles = dataExtractor.findRecommendations(title.animeTitle());
        for (AnimeTitle animeTitle : recommendedTitles) {
            if (getToExclude().contains(animeTitle))
                continue;
            recommendedAnime.merge(animeTitle, title.score(), Integer::sum);
        }
    }
}

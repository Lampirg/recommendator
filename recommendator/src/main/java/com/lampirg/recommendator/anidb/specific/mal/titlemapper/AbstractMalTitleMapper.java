package com.lampirg.recommendator.anidb.specific.mal.titlemapper;

import com.lampirg.recommendator.anidb.general.titlemapper.IterativeTitleMapper;
import com.lampirg.recommendator.anidb.general.titlemapper.TitleMapper;
import com.lampirg.recommendator.anidb.specific.mal.MalQueryMaker;
import com.lampirg.recommendator.anidb.specific.mal.MalChacher;
import com.lampirg.recommendator.anidb.titles.model.AnimeTitle;
import com.lampirg.recommendator.anidb.titles.model.UserAnimeTitle;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public abstract class AbstractMalTitleMapper extends IterativeTitleMapper implements TitleMapper {

    private MalQueryMaker queryMaker;
    private MalChacher repository;

    @Autowired
    public void setQueryMaker(MalQueryMaker queryMaker) {
        this.queryMaker = queryMaker;
    }
    @Autowired
    public void setRepository(MalChacher repository) {
        this.repository = repository;
    }

    protected final void findAndAddTitleRecommendations(UserAnimeTitle title) {
        Set<AnimeTitle> recommendedTitles = repository.getRecommendations(title.animeTitle(), queryMaker);
        for (AnimeTitle animeTitle : recommendedTitles) {
            if (getToExclude().contains(animeTitle))
                continue;
            recommendedAnime.merge(animeTitle, title.score(), Integer::sum);
        }
    }
}

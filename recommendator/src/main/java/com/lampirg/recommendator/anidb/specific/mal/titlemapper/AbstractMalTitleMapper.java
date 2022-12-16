package com.lampirg.recommendator.anidb.specific.mal.titlemapper;

import com.lampirg.recommendator.anidb.general.titlemapper.IterativeTitleMapper;
import com.lampirg.recommendator.anidb.general.titlemapper.TitleMapper;
import com.lampirg.recommendator.anidb.specific.mal.MalQueryMaker;
import com.lampirg.recommendator.anidb.specific.mal.json.Recommendation;
import com.lampirg.recommendator.anidb.specific.mal.json.queries.GetAnimeDetail;
import com.lampirg.recommendator.anidb.titles.model.AnimeTitle;
import com.lampirg.recommendator.anidb.titles.model.UserAnimeTitle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.*;

public abstract class AbstractMalTitleMapper extends IterativeTitleMapper implements TitleMapper {

    private MalQueryMaker queryMaker;

    @Autowired
    public void setQueryMaker(MalQueryMaker queryMaker) {
        this.queryMaker = queryMaker;
    }

    protected final void findAndAddTitleRecommendations(UserAnimeTitle title) {
        String url = "https://api.myanimelist.net/v2/anime/"+title.animeTitle().id()+"?fields=recommendations";
        ResponseEntity<GetAnimeDetail> response = queryMaker.exchange(url, HttpMethod.GET, getRequest(), GetAnimeDetail.class);
        for (Recommendation recommendation : Objects.requireNonNull(response.getBody()).recommendations()) {
            AnimeTitle animeTitle = AnimeTitle.retrieveFromMalNode(recommendation.node());
            if (getToExclude().contains(animeTitle))
                continue;
            recommendedAnime.merge(animeTitle, title.score(), Integer::sum);
        }
    }
}

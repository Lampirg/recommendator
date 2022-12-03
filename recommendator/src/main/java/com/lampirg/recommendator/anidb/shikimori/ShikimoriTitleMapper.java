package com.lampirg.recommendator.anidb.shikimori;

import com.lampirg.recommendator.anidb.IterativeTitleMapper;
import com.lampirg.recommendator.anidb.TitleMapper;
import com.lampirg.recommendator.anidb.model.AnimeTitle;
import com.lampirg.recommendator.anidb.model.UserAnimeTitle;
import com.lampirg.recommendator.anidb.shikimori.json.ShikiNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Qualifier("shiki")
@Scope("prototype")
public class ShikimoriTitleMapper extends IterativeTitleMapper implements TitleMapper {

    private ShikimoriQueryMaker queryMaker;

    @Autowired
    public void setQueryMaker(ShikimoriQueryMaker queryMaker) {
        this.queryMaker = queryMaker;
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
        String url = "https://shikimori.one/api/animes/"+title.animeTitle().id()+"/similar";
        ResponseEntity<List<ShikiNode>> response = queryMaker.exchange(url, HttpMethod.GET, getRequest(), new ParameterizedTypeReference<>() {});
        for (ShikiNode recommendation : Objects.requireNonNull(response.getBody())) {
            AnimeTitle animeTitle = AnimeTitle.retrieveFromShikiNode(recommendation);
            if (getToExclude().contains(animeTitle))
                continue;
            recommendedAnime.merge(animeTitle, title.score(), Integer::sum);
        }
    }
}

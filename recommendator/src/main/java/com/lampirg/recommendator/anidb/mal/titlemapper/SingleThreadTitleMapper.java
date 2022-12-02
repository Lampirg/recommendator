package com.lampirg.recommendator.anidb.mal.titlemapper;

import com.lampirg.recommendator.anidb.mal.MalQueryMaker;
import com.lampirg.recommendator.anidb.mal.json.Recommendation;
import com.lampirg.recommendator.anidb.mal.json.queries.GetAnimeDetail;
import com.lampirg.recommendator.anidb.model.AnimeTitle;
import com.lampirg.recommendator.anidb.model.UserAnimeTitle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
@Qualifier("single")
@Scope("prototype")
public class SingleThreadTitleMapper implements TitleMapper {

    MalQueryMaker queryMaker;
    HttpEntity<String> request;

    private final Map<AnimeTitle, Integer> recommendedAnime = new HashMap<>();
    private Set<AnimeTitle> toExclude = new HashSet<>();

    @Autowired
    public void setQueryMaker(MalQueryMaker queryMaker) {
        this.queryMaker = queryMaker;
    }

    @Override
    public TitleMapper setRequest(HttpEntity<String> request) {
        this.request = request;
        return this;
    }

    @Override
    public Map<AnimeTitle, Integer> getRecommendedAnimeMap(Set<UserAnimeTitle> animeTitles) {
        if (!recommendedAnime.isEmpty())
            return recommendedAnime;
        for (UserAnimeTitle title : animeTitles) {
            findAndAddTitleRecommendations(title);
        }
        return recommendedAnime;
    }

    public TitleMapper fillToExclude(Set<UserAnimeTitle> toExclude) {
        for (UserAnimeTitle title : toExclude) {
            this.toExclude.add(title.animeTitle());
        }
        return this;
    }

    private void findAndAddTitleRecommendations(UserAnimeTitle title) {
        String url = "https://api.myanimelist.net/v2/anime/"+title.animeTitle().id()+"?fields=recommendations";
        ResponseEntity<GetAnimeDetail> response = queryMaker.exchange(url, HttpMethod.GET, request, GetAnimeDetail.class);
        for (Recommendation recommendation : Objects.requireNonNull(response.getBody()).recommendations()) {
            AnimeTitle animeTitle = AnimeTitle.retrieveFromMalNode(recommendation.node());
            if (toExclude.contains(animeTitle))
                continue;
            recommendedAnime.merge(animeTitle, title.score(), Integer::sum);
        }
    }
}

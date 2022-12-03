package com.lampirg.recommendator.anidb;

import com.lampirg.recommendator.anidb.mal.MalQueryMaker;
import com.lampirg.recommendator.anidb.model.AnimeTitle;
import com.lampirg.recommendator.anidb.model.UserAnimeTitle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class IterativeTitleMapper implements TitleMapper {
    private MalQueryMaker queryMaker;
    private HttpEntity<String> request;
    private Set<AnimeTitle> toExclude = new HashSet<>();

    protected final MalQueryMaker getQueryMaker() {
        return queryMaker;
    }

    protected final HttpEntity<String> getRequest() {
        return request;
    }

    protected final Set<AnimeTitle> getToExclude() {
        return toExclude;
    }

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
    public abstract Map<AnimeTitle, Integer> getRecommendedAnimeMap(Set<UserAnimeTitle> animeTitles);

    public TitleMapper fillToExclude(Set<UserAnimeTitle> toExclude) {
        for (UserAnimeTitle title : toExclude) {
            this.toExclude.add(title.animeTitle());
        }
        return this;
    }

    protected abstract void findAndAddTitleRecommendations(UserAnimeTitle title);
}

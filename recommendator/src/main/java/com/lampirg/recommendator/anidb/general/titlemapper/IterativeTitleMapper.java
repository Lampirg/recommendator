package com.lampirg.recommendator.anidb.general.titlemapper;

import com.lampirg.recommendator.anidb.titles.model.AnimeTitle;
import com.lampirg.recommendator.anidb.titles.model.UserAnimeTitle;
import org.springframework.http.HttpEntity;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class IterativeTitleMapper implements TitleMapper {
    private HttpEntity<String> request;
    protected Map<AnimeTitle, Integer> recommendedAnime;
    private Set<AnimeTitle> toExclude = new HashSet<>();

    protected final HttpEntity<String> getRequest() {
        return request;
    }

    protected final Set<AnimeTitle> getToExclude() {
        return toExclude;
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

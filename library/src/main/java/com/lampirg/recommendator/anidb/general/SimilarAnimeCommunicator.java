package com.lampirg.recommendator.anidb.general;

import com.lampirg.recommendator.anidb.general.listextractor.UserListExtractor;
import com.lampirg.recommendator.anidb.titles.model.AnimeRecommendation;
import com.lampirg.recommendator.anidb.titles.model.AnimeTitle;
import com.lampirg.recommendator.anidb.titles.model.UserAnimeTitle;
import com.lampirg.recommendator.anidb.general.titlemapper.TitleMapper;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
@Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
public class SimilarAnimeCommunicator implements AnimeSiteCommunicator {

    private UserListExtractor userListExtractor;
    private TitleMapper titleMapper;


    public void setListExtractor(UserListExtractor userListExtractor) {
        this.userListExtractor = userListExtractor;
    }
    public void setTitleMapper(TitleMapper titleMapper) {
        this.titleMapper = titleMapper;
    }

    @Override
    public Set<AnimeRecommendation> getSimilarAnimeTitles(String username) {
        return getSimilarAnimeTitles(
                userListExtractor.getToInclude(username), userListExtractor.getToExclude(username)
        );
    }

    public Set<AnimeRecommendation> getSimilarAnimeTitles(Set<UserAnimeTitle> animeTitles, Set<UserAnimeTitle> toExclude) {
        Map<AnimeTitle, Integer> result = titleMapper.fillToExclude(toExclude)
                .getRecommendedAnimeMap(animeTitles);
        Set<AnimeRecommendation> animeRecommendationSet = new HashSet<>();
        result.forEach((key, value) -> animeRecommendationSet.add(new AnimeRecommendation(key, value)));
        return Set.copyOf(animeRecommendationSet);
    }
}

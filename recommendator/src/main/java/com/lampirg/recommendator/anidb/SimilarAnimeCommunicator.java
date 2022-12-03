package com.lampirg.recommendator.anidb;

import com.lampirg.recommendator.anidb.model.AnimeRecommendation;
import com.lampirg.recommendator.anidb.model.AnimeTitle;
import com.lampirg.recommendator.anidb.model.UserAnimeTitle;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
@Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
@PropertySource("classpath:mal security code.yml")
@Qualifier("mal-default")
public class SimilarAnimeCommunicator implements AnimeSiteCommunicator {

    private UserListExtractor userListExtractor;
    private TitleMapper titleMapper;

    @Value("${clientIdHeader}")
    private String clientIdHeader;
    @Value("${clientId}")
    private String clientId;
    HttpEntity<String> request;

    public void setListExtractor(UserListExtractor userListExtractor) {
        this.userListExtractor = userListExtractor;
    }
    public void setTitleMapper(TitleMapper titleMapper) {
        this.titleMapper = titleMapper;
    }

    @PostConstruct
    private void init() {
        HttpHeaders authHeader = new HttpHeaders();
        authHeader.set(clientIdHeader, clientId);
        request = new HttpEntity<>(authHeader);
    }

    @Override
    public Set<AnimeRecommendation> getSimilarAnimeTitles(String username) {
        userListExtractor.setRequest(request).setUser(username);
        return getSimilarAnimeTitles(userListExtractor.getToInclude(), userListExtractor.getToExclude());
    }

    public Set<AnimeRecommendation> getSimilarAnimeTitles(Set<UserAnimeTitle> animeTitles, Set<UserAnimeTitle> toExclude) {
        Map<AnimeTitle, Integer> result = titleMapper.setRequest(request).fillToExclude(toExclude)
                .getRecommendedAnimeMap(animeTitles);
        Set<AnimeRecommendation> animeRecommendationSet = new HashSet<>();
        result.forEach((key, value) -> animeRecommendationSet.add(new AnimeRecommendation(key, value)));
        return Set.copyOf(animeRecommendationSet);
    }
}

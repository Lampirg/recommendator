package com.lampirg.recommendator.anidb.mal;

import com.lampirg.recommendator.anidb.AnimeSiteCommunicator;
import com.lampirg.recommendator.anidb.mal.json.Data;
import com.lampirg.recommendator.anidb.mal.json.queries.GetUserListJsonResult;
import com.lampirg.recommendator.anidb.mal.querymaker.QueryMaker;
import com.lampirg.recommendator.anidb.mal.titlemapper.TitleMapper;
import com.lampirg.recommendator.anidb.model.AnimeRecommendation;
import com.lampirg.recommendator.anidb.model.AnimeTitle;
import com.lampirg.recommendator.anidb.model.UserAnimeTitle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
@PropertySource("classpath:mal security code.yml")
@Qualifier("default")
public class MalCommunicator implements AnimeSiteCommunicator {

    private QueryMaker queryMaker;
    private TitleMapper titleMapper;

    @Value("${clientIdHeader}")
    private String clientIdHeader;
    @Value("${clientId}")
    private String clientId;
    HttpEntity<String> request;

    public void setQueryMaker(QueryMaker queryMaker) {
        this.queryMaker = queryMaker;
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
        queryMaker.setRequest(request).setUser(username);
        return getSimilarAnimeTitles(queryMaker.getToInclude(), queryMaker.getToExclude());
    }

    public Set<AnimeRecommendation> getSimilarAnimeTitles(Set<UserAnimeTitle> animeTitles, Set<UserAnimeTitle> toExclude) {
        Map<AnimeTitle, Integer> result = titleMapper.setRequest(request).fillToExclude(toExclude)
                .getRecommendedAnimeMap(animeTitles);
        Set<AnimeRecommendation> animeRecommendationSet = new HashSet<>();
        result.forEach((key, value) -> animeRecommendationSet.add(new AnimeRecommendation(key, value)));
        return Set.copyOf(animeRecommendationSet);
    }
}

package com.lampirg.frontend.service;

import com.lampirg.frontend.controller.FrontendController;
import com.lampirg.recommendator.anidb.general.AnimeSiteCommunicator;
import com.lampirg.recommendator.anidb.titles.model.AnimeRecommendation;
import com.lampirg.recommendator.anidb.titles.model.AnimeRecommendationList;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RecommendatorCommunicator implements AnimeRecommendationService  {

    private final RestTemplate restTemplate;

    @Override
    public AnimeRecommendationList getAnimeRecommendationList(HttpHeaders headers) {
        return Optional.ofNullable(restTemplate.exchange(
                "http://recommendator/recommend", HttpMethod.GET, new HttpEntity<>(headers),
                AnimeRecommendationList.class
        ).getBody()).orElseThrow();
    }
}

package com.lampirg.frontend.service;

import com.lampirg.recommendator.anidb.titles.model.AnimeRecommendationList;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.util.Optional;

public interface AnimeRecommendationService {
     AnimeRecommendationList getAnimeRecommendationList(HttpHeaders headers);
}

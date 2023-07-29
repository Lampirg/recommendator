package com.lampirg.recommendator.controller;

import com.lampirg.recommendator.anidb.titles.model.AnimeRecommendationList;
import com.lampirg.recommendator.model.Request;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/recommend")
public class SimilarAnimeController {

    private RestTemplate restTemplate;

    public SimilarAnimeController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public ResponseEntity<?> getSimilarAnime(@RequestHeader String service, @RequestHeader String username) {
        return restTemplate.getForEntity(
                getUrl(service, username),
                AnimeRecommendationList.class
        );
    }

    private String getUrl(String service, String username) {
        return "http://%s/%s/%s".formatted(service, service, username);
    }
}

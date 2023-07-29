package com.lampirg.recommendator.controller;

import com.lampirg.recommendator.anidb.titles.model.AnimeRecommendationList;
import com.lampirg.recommendator.model.Request;
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

    // TODO: replace get request with post request
    @GetMapping
    public ResponseEntity<?> getSimilarAnime(@RequestBody Request request) {
        return restTemplate.getForEntity(
                getUrl(request.service(), request.username()),
                AnimeRecommendationList.class
        );
    }

    private String getUrl(String service, String username) {
        return "http://%s/%s/%s".formatted(service, service, username);
    }
}

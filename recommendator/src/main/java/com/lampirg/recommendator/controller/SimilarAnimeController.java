package com.lampirg.recommendator.controller;

import com.lampirg.recommendator.anidb.general.AnimeSiteCommunicator;
import com.lampirg.recommendator.anidb.titles.model.AnimeRecommendation;
import com.lampirg.recommendator.anidb.titles.model.AnimeRecommendationList;
import com.lampirg.recommendator.model.Request;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/recommend")
public class SimilarAnimeController {

    private RestTemplate restTemplate;

    public SimilarAnimeController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public ResponseEntity<?> getMalSimilarAnime(@RequestBody Request request) {
        return restTemplate.getForEntity(
                getUrl(request.service(), request.username()),
                AnimeRecommendationList.class
        );
    }

    private String getUrl(String service, String username) {
        return "http://" + service + "/mal/" + username;
    }
}

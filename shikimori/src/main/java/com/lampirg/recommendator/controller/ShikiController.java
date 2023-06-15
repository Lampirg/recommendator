package com.lampirg.recommendator.controller;

import com.lampirg.recommendator.anidb.general.AnimeSiteCommunicator;
import com.lampirg.recommendator.anidb.titles.model.AnimeRecommendation;
import com.lampirg.recommendator.anidb.titles.model.AnimeRecommendationList;
import com.lampirg.recommendator.config.qualifiers.Shiki;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/shiki")
public class ShikiController {

    private AnimeSiteCommunicator shikiCommunicator;


    @Autowired
    public void setShikiCommunicator(@Shiki AnimeSiteCommunicator siteCommunicator) {
        this.shikiCommunicator = siteCommunicator;
    }

    @GetMapping("/{username}")
    public AnimeRecommendationList getShikiSimilarAnime(@PathVariable String username) {
        return getSortedRecommendationList(shikiCommunicator, username);
    }

    private AnimeRecommendationList getSortedRecommendationList(AnimeSiteCommunicator malCommunicator, String username) {
        List<AnimeRecommendation> list =
                new ArrayList<>(malCommunicator.getSimilarAnimeTitles(username));
        list.sort(Comparator.comparingInt(AnimeRecommendation::numOfRecommendations).reversed()
                .thenComparing(x -> x.title().name()));
        return new AnimeRecommendationList(list);
    }

    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> notFound(HttpClientErrorException exception) {
        return new ResponseEntity<>("user not found", HttpStatus.NOT_FOUND);
    }
}

package com.lampirg.recommendator.controller;

import com.lampirg.recommendator.anidb.general.AnimeSiteCommunicator;
import com.lampirg.recommendator.anidb.general.SimilarAnimeCommunicator;
import com.lampirg.recommendator.anidb.titles.model.AnimeRecommendation;
import com.lampirg.recommendator.anidb.titles.model.AnimeRecommendationList;
import com.lampirg.recommendator.config.qualifiers.Mal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/mal")
public class MalController {

    private SimilarAnimeCommunicator malCommunicator;

    public MalController(@Mal("single") SimilarAnimeCommunicator malCommunicator) {
        this.malCommunicator = malCommunicator;
    }

    @GetMapping("/{username}")
    public AnimeRecommendationList getMalSimilarAnime(@PathVariable String username) {
        return getSortedRecommendationList(malCommunicator, username);
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

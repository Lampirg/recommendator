package com.lampirg.recommendator.controller;

import com.lampirg.recommendator.anidb.AnimeSiteCommunicator;
import com.lampirg.recommendator.anidb.model.AnimeRecommendation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/recommend")
public class SimilarAnimeController {

    private AnimeSiteCommunicator malCommunicator;
    private AnimeSiteCommunicator shikiCommunicator;

    @Autowired
    public void setMalCommunicator(@Qualifier("mal-single") AnimeSiteCommunicator siteCommunicator) {
        this.malCommunicator = siteCommunicator;
    }

    @Autowired
    public void setShikiCommunicator(@Qualifier("shiki") AnimeSiteCommunicator siteCommunicator) {
        this.shikiCommunicator = siteCommunicator;
    }

    @GetMapping("/mal/{username}")
    public List<AnimeRecommendation> getMalSimilarAnime(@PathVariable String username) {
        List<AnimeRecommendation> list =
                new ArrayList<>(malCommunicator.getSimilarAnimeTitles(username));
        list.sort(Comparator.comparingInt(AnimeRecommendation::numOfRecommendations).reversed()
                .thenComparing(x -> x.title().name()));
        return list;
    }

    @GetMapping("/shiki/{username}")
    public List<AnimeRecommendation> getShikiSimilarAnime(@PathVariable String username) {
        List<AnimeRecommendation> list =
                new ArrayList<>(shikiCommunicator.getSimilarAnimeTitles(username));
        list.sort(Comparator.comparingInt(AnimeRecommendation::numOfRecommendations).reversed()
                .thenComparing(x -> x.title().name()));
        return list;
    }

}

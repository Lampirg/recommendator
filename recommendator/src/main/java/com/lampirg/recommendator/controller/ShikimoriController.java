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
@RequestMapping("/mal")
public class ShikimoriController {

    private AnimeSiteCommunicator siteCommunicator;

    @Autowired
    public void setSiteCommunicator(@Qualifier("shikimori") AnimeSiteCommunicator siteCommunicator) {
        this.siteCommunicator = siteCommunicator;
    }

    @GetMapping("/{username}")
    public List<AnimeRecommendation> getSimilarAnime(@PathVariable String username) {
        List<AnimeRecommendation> list =
                new ArrayList<>(siteCommunicator.getSimilarAnimeTitles(username));
        list.sort(Comparator.comparingInt(AnimeRecommendation::numOfRecommendations).reversed()
                .thenComparing(x -> x.title().name()));
        return list;
    }

}
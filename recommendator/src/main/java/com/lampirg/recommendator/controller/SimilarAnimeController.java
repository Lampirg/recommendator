package com.lampirg.recommendator.controller;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.lampirg.recommendator.anidb.AnimeSiteCommunicator;
import com.lampirg.recommendator.model.AnimeRecommendation;
import com.lampirg.recommendator.model.AnimeTitle;
import com.lampirg.recommendator.model.UserAnimeTitle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/recommend")
public class SimilarAnimeController {

    @Autowired
    private AnimeSiteCommunicator siteCommunicator;

    @GetMapping("/{nickname}")
    public List<AnimeRecommendation> getSimilarAnime(@PathVariable String nickname) {
        List<AnimeRecommendation> list =
                new ArrayList<>(siteCommunicator.getSimilarAnimeTitles(siteCommunicator.getUserAnimeList(nickname)));
        list.sort(Comparator.comparingInt(AnimeRecommendation::numOfRecommendations).reversed());
        return list;
    }

}

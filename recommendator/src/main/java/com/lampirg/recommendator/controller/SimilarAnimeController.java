package com.lampirg.recommendator.controller;

import com.lampirg.recommendator.anidb.general.AnimeSiteCommunicator;
import com.lampirg.recommendator.anidb.general.model.AnimeRecommendation;
import com.lampirg.recommendator.config.quilifiers.Anilist;
import com.lampirg.recommendator.config.quilifiers.Mal;
import com.lampirg.recommendator.config.quilifiers.Shiki;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
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
    private AnimeSiteCommunicator anilistCommunicator;

    @Autowired
    @Lazy
    public void setMalCommunicator(@Mal("single") AnimeSiteCommunicator siteCommunicator) {
        this.malCommunicator = siteCommunicator;
    }

    @Autowired
    @Lazy
    public void setShikiCommunicator(@Shiki AnimeSiteCommunicator siteCommunicator) {
        this.shikiCommunicator = siteCommunicator;
    }

    @Autowired
    @Lazy
    public void setAnilistCommunicator(@Anilist AnimeSiteCommunicator siteCommunicator) {
        this.anilistCommunicator = siteCommunicator;
    }

    @GetMapping("/mal/{username}")
    public List<AnimeRecommendation> getMalSimilarAnime(@PathVariable String username) {
        return getSortedRecommendationList(malCommunicator, username);
    }

    @GetMapping("/shiki/{username}")
    public List<AnimeRecommendation> getShikiSimilarAnime(@PathVariable String username) {
        return getSortedRecommendationList(shikiCommunicator, username);
    }

    @GetMapping("/anilist/{username}")
    public List<AnimeRecommendation> getAnilistSimilarAnime(@PathVariable String username) {
        return getSortedRecommendationList(anilistCommunicator, username);
    }

    private List<AnimeRecommendation> getSortedRecommendationList(AnimeSiteCommunicator malCommunicator, String username) {
        List<AnimeRecommendation> list =
                new ArrayList<>(malCommunicator.getSimilarAnimeTitles(username));
        list.sort(Comparator.comparingInt(AnimeRecommendation::numOfRecommendations).reversed()
                .thenComparing(x -> x.title().name()));
        return list;
    }
}

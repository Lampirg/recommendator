package com.lampirg.frontend.controller;

import com.lampirg.frontend.service.AnimeRecommendationService;
import com.lampirg.recommendator.anidb.titles.model.AnimeRecommendationList;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/recommend")
@RequiredArgsConstructor
public class FrontendController {

    private final AnimeRecommendationService communicator;

    @GetMapping
    public String mainPage(Model model) {
        model.addAttribute("services", Arrays.stream(Services.values()).toList());
        model.addAttribute("request", new Request());
        return "main";
    }

    @PostMapping
    public ModelAndView getRecommendations(Request request) {
        HttpHeaders headers = new HttpHeaders();
        headers.putAll(Map.of(
                "service", List.of(request.getService()),
                "username", List.of(request.getUsername())
        ));
        AnimeRecommendationList list = communicator.getAnimeRecommendationList(headers);
        ModelAndView mav = new ModelAndView("recommendations");
        mav.addObject("list", list.animeRecommendations());
        return mav;
    }

    @NoArgsConstructor
    @Setter
    @Getter
    public static class Request {
        private String service;
        private String username;
    }

    @RequiredArgsConstructor
    @Getter
    public enum Services {
        MAL("mal", "My Anime List"),
        SHIKI("shiki", "Shikimori"),
        ANILIST("anilist", "Anilist");
        private final String name;
        private final String beautyName;
    }
}

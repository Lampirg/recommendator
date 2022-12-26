package com.lampirg.recommendator.anidb.titles.model;

import java.util.List;

public record AnimeRecommendationList(
        List<AnimeRecommendation> animeRecommendations
) {
}

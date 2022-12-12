package com.lampirg.recommendator.anidb.specific.mal.json;

import com.fasterxml.jackson.annotation.JsonProperty;


public record Recommendation(
        MalNode node,
        @JsonProperty("num_recommendations")
        int numberOfRecommendations
) {
}

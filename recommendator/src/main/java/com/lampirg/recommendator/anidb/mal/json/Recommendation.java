package com.lampirg.recommendator.anidb.mal.json;

import com.fasterxml.jackson.annotation.JsonProperty;


public record Recommendation(
        Node node,
        @JsonProperty("num_recommendations")
        int numberOfRecommendations
) {
}

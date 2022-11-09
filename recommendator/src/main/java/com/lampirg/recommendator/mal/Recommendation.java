package com.lampirg.recommendator.mal;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


public record Recommendation(
        Node node,
        @JsonProperty("num_recommendations")
        int numberOfRecommendations
) {
}

package com.lampirg.recommendator.mal.queries;

import com.lampirg.recommendator.mal.Recommendation;

import java.util.List;

public record GetAnimeDetail(
        List<Recommendation> recommendations
) {
}

package com.lampirg.recommendator.anidb.mal.json.queries;

import com.lampirg.recommendator.anidb.mal.json.Recommendation;

import java.util.List;

public record GetAnimeDetail(
        List<Recommendation> recommendations
) {
}

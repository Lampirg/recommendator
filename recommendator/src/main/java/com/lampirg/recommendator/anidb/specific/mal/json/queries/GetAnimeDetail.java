package com.lampirg.recommendator.anidb.specific.mal.json.queries;

import com.lampirg.recommendator.anidb.specific.mal.json.Recommendation;

import java.util.List;

public record GetAnimeDetail(
        List<Recommendation> recommendations
) {
}

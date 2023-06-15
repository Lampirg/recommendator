package com.lampirg.recommendator.anidb.json.queries;

import com.lampirg.recommendator.anidb.json.Recommendation;

import java.util.List;

public record GetAnimeDetail(
        List<Recommendation> recommendations
) {
}

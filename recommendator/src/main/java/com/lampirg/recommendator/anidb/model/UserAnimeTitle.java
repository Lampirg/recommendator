package com.lampirg.recommendator.anidb.model;

public record UserAnimeTitle(
        AnimeTitle animeTitle,
        int score
) {
}

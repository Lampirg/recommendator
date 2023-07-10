package com.lampirg.recommendator.anidb;

import com.lampirg.recommendator.anidb.json.Completed;
import com.lampirg.recommendator.anidb.json.Other;
import com.lampirg.recommendator.anidb.titles.model.AnimeTitle;
import io.micrometer.core.instrument.util.IOUtils;
import lombok.SneakyThrows;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class Utils {
    public static AnimeTitle retrieveFromAnilistMedia(Other.Lists.Entries.Media media) {
        return new AnimeTitle(media.id(), media.title().romaji(), media.coverImage().extraLarge().replaceAll("\\\\", ""));
    }
    public static AnimeTitle retrieveFromAnilistMedia(Completed.CompletedList.Entries.Media media) {
        return new AnimeTitle(media.id(), media.title().romaji(), media.coverImage().extraLarge().replaceAll("\\\\", ""));
    }
    public static AnimeTitle retrieveFromAnilistMedia(Completed.CompletedList.Entries.Media.Recommendations.Nodes.MediaRecommendation media) {
        return new AnimeTitle(media.id(), media.title().romaji(), media.coverImage().extraLarge().replaceAll("\\\\", ""));
    }
    @SneakyThrows
    public static String resourceToString(Resource resource) {
        try(InputStream inputStream = resource.getInputStream()) {
            return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        }
    }
}

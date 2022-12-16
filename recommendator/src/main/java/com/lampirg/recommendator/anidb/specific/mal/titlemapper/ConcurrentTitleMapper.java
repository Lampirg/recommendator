package com.lampirg.recommendator.anidb.specific.mal.titlemapper;

import com.lampirg.recommendator.anidb.general.titlemapper.TitleMapper;
import com.lampirg.recommendator.anidb.titles.model.AnimeTitle;
import com.lampirg.recommendator.anidb.titles.model.UserAnimeTitle;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

public class ConcurrentTitleMapper extends AbstractMalTitleMapper implements TitleMapper {
    @Override
    public Map<AnimeTitle, Integer> getRecommendedAnimeMap(Set<UserAnimeTitle> animeTitles) {
        if (recommendedAnime == null)
            recommendedAnime = new ConcurrentHashMap<>();
        if (!recommendedAnime.isEmpty())
            return recommendedAnime;
        CompletableFuture<Void> future = CompletableFuture.allOf();
        for (UserAnimeTitle title : animeTitles) {
            future = CompletableFuture.allOf(future,
                    CompletableFuture.runAsync(() -> findAndAddTitleRecommendations(title))
            );
        }
        future.join();
        return recommendedAnime;
    }
}

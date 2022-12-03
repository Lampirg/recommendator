package com.lampirg.recommendator.anidb.mal.titlemapper;

import com.lampirg.recommendator.anidb.general.TitleMapper;
import com.lampirg.recommendator.anidb.general.model.AnimeTitle;
import com.lampirg.recommendator.anidb.general.model.UserAnimeTitle;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

// TODO: fix thread unsafety
@Component
@Qualifier("concurrent")
@Scope("prototype")
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

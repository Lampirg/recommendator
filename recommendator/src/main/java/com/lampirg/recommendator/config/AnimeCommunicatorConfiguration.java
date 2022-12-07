package com.lampirg.recommendator.config;

import com.lampirg.recommendator.anidb.anilist.AnilistCommunicator;
import com.lampirg.recommendator.anidb.general.AnimeSiteCommunicator;
import com.lampirg.recommendator.anidb.general.SimilarAnimeCommunicator;
import com.lampirg.recommendator.anidb.general.TitleMapper;
import com.lampirg.recommendator.anidb.general.UserListExtractor;
import com.lampirg.recommendator.anidb.mal.listextractor.ConcurrentUserListExtractor;
import com.lampirg.recommendator.anidb.mal.listextractor.SingleThreadUserListExtractor;
import com.lampirg.recommendator.anidb.mal.titlemapper.ConcurrentTitleMapper;
import com.lampirg.recommendator.anidb.mal.titlemapper.SingleThreadTitleMapper;
import com.lampirg.recommendator.anidb.shikimori.ShikimoriListExtractor;
import com.lampirg.recommendator.anidb.shikimori.ShikimoriTitleMapper;
import com.lampirg.recommendator.config.quilifiers.Mal;
import com.lampirg.recommendator.config.quilifiers.Shiki;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;

@Configuration
@Import({ListExtractorConfiguration.class, TitleMapperConfiguration.class})
@ComponentScan("com.lampirg.recommendator.anidb")
public class AnimeCommunicatorConfiguration {

    @Bean
    @Mal("single")
    public SimilarAnimeCommunicator singleThreadMalCommunicator(@Mal("single") UserListExtractor listExtractor,
                                                                @Mal("single") TitleMapper mapper) {
        return getSimilarAnimeCommunicator(listExtractor, mapper);
    }

    @Bean
    @Mal("concurrent")
    public SimilarAnimeCommunicator concurrentThreadMalCommunicator(@Mal("concurrent") UserListExtractor listExtractor,
                                                                    @Mal("concurrent") TitleMapper mapper) {
        return getSimilarAnimeCommunicator(listExtractor, mapper);
    }

    @Bean
    @Shiki
    public SimilarAnimeCommunicator shikimoriCommunicator(@Shiki UserListExtractor listExtractor,
                                                          @Shiki TitleMapper mapper) {
        return getSimilarAnimeCommunicator(listExtractor, mapper);
    }

    @Bean
    @Qualifier("anilist")
    public AnimeSiteCommunicator anilistCommunicator() {
        return new AnilistCommunicator();
    }

    private SimilarAnimeCommunicator getSimilarAnimeCommunicator(UserListExtractor listExtractor, TitleMapper mapper) {
        SimilarAnimeCommunicator communicator = new SimilarAnimeCommunicator();
        communicator.setListExtractor(listExtractor);
        communicator.setTitleMapper(mapper);
        return communicator;
    }
}

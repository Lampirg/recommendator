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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;

@Configuration
@Import({ListExtractorConfiguration.class, TitleMapperConfiguration.class})
@ComponentScan("com.lampirg.recommendator.anidb")
public class AnimeCommunicatorConfiguration {

    @Bean
    @Qualifier("mal-single")
    public SimilarAnimeCommunicator singleThreadMalCommunicator(@Qualifier("mal-single") UserListExtractor listExtractor,
                                                                @Qualifier("mal-single") TitleMapper mapper) {
        return getSimilarAnimeCommunicator(listExtractor, mapper);
    }

    @Bean
    @Qualifier("mal-concurrent")
    public SimilarAnimeCommunicator concurrentThreadMalCommunicator(@Qualifier("mal-concurrent") UserListExtractor listExtractor,
                                                                    @Qualifier("mal-concurrent") TitleMapper mapper) {
        return getSimilarAnimeCommunicator(listExtractor, mapper);
    }

    @Bean
    @Qualifier("shiki")
    public SimilarAnimeCommunicator shikimoriCommunicator(@Qualifier("shiki") UserListExtractor listExtractor,
                                                          @Qualifier("shiki") TitleMapper mapper) {
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

package com.lampirg.recommendator.config;

import com.lampirg.recommendator.anidb.specific.anilist.AnilistCommunicator;
import com.lampirg.recommendator.anidb.general.AnimeSiteCommunicator;
import com.lampirg.recommendator.anidb.general.SimilarAnimeCommunicator;
import com.lampirg.recommendator.anidb.general.TitleMapper;
import com.lampirg.recommendator.anidb.general.UserListExtractor;
import com.lampirg.recommendator.config.qualifiers.Anilist;
import com.lampirg.recommendator.config.qualifiers.Mal;
import com.lampirg.recommendator.config.qualifiers.Shiki;
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
    @Anilist
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

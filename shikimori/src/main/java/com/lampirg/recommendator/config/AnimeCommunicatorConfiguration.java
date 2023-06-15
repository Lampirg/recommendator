package com.lampirg.recommendator.config;

import com.lampirg.recommendator.anidb.general.SimilarAnimeCommunicator;
import com.lampirg.recommendator.anidb.general.listextractor.UserListExtractor;
import com.lampirg.recommendator.anidb.general.titlemapper.TitleMapper;
import com.lampirg.recommendator.config.qualifiers.Shiki;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ListExtractorConfiguration.class, TitleMapperConfiguration.class})
@ComponentScan("com.lampirg.recommendator.anidb")
public class AnimeCommunicatorConfiguration {

    @Bean
    @Shiki
    public SimilarAnimeCommunicator shikimoriCommunicator(@Shiki UserListExtractor listExtractor,
                                                          @Shiki TitleMapper mapper) {
        return attachBeansToCommunicator(new SimilarAnimeCommunicator(), listExtractor, mapper);
    }

    private SimilarAnimeCommunicator attachBeansToCommunicator(SimilarAnimeCommunicator communicator, UserListExtractor listExtractor, TitleMapper mapper) {
        communicator.setListExtractor(listExtractor);
        communicator.setTitleMapper(mapper);
        return communicator;
    }
}

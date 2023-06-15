package com.lampirg.recommendator.config;

import com.lampirg.recommendator.anidb.general.SimilarAnimeCommunicator;
import com.lampirg.recommendator.anidb.general.listextractor.UserListExtractor;
import com.lampirg.recommendator.anidb.general.titlemapper.TitleMapper;
import com.lampirg.recommendator.config.qualifiers.Mal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ListExtractorConfiguration.class, TitleMapperConfiguration.class})
@ComponentScan("com.lampirg.recommendator.anidb")
public class AnimeCommunicatorConfiguration {

    @Bean
    @Mal("single")
    public SimilarAnimeCommunicator singleThreadMalCommunicator(@Mal("single") UserListExtractor listExtractor,
                                                                @Mal("single") TitleMapper mapper) {
        return attachBeansToCommunicator(new SimilarAnimeCommunicator(), listExtractor, mapper);
    }

    @Bean
    @Mal("concurrent")
    public SimilarAnimeCommunicator concurrentThreadMalCommunicator(@Mal("concurrent") UserListExtractor listExtractor,
                                                                    @Mal("concurrent") TitleMapper mapper) {
        return attachBeansToCommunicator(new SimilarAnimeCommunicator(), listExtractor, mapper);
    }

    private SimilarAnimeCommunicator attachBeansToCommunicator(SimilarAnimeCommunicator communicator, UserListExtractor listExtractor, TitleMapper mapper) {
        communicator.setListExtractor(listExtractor);
        communicator.setTitleMapper(mapper);
        return communicator;
    }
}

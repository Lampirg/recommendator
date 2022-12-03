package com.lampirg.recommendator.config;

import com.lampirg.recommendator.anidb.general.SimilarAnimeCommunicator;
import com.lampirg.recommendator.anidb.mal.listextractor.ConcurrentUserListExtractor;
import com.lampirg.recommendator.anidb.mal.listextractor.SingleThreadUserListExtractor;
import com.lampirg.recommendator.anidb.mal.titlemapper.ConcurrentTitleMapper;
import com.lampirg.recommendator.anidb.mal.titlemapper.SingleThreadTitleMapper;
import com.lampirg.recommendator.anidb.shikimori.ShikimoriListExtractor;
import com.lampirg.recommendator.anidb.shikimori.ShikimoriTitleMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScan("com.lampirg.recommendator.anidb")
public class AnimeCommunicatorConfiguration {

    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
    @Qualifier("mal-single")
    public SimilarAnimeCommunicator singleThreadMalCommunicator(SingleThreadUserListExtractor listExtractor,
                                                                SingleThreadTitleMapper mapper) {
        SimilarAnimeCommunicator communicator = new SimilarAnimeCommunicator();
        communicator.setListExtractor(listExtractor);
        communicator.setTitleMapper(mapper);
        return communicator;
    }

    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
    @Qualifier("mal-concurrent")
    public SimilarAnimeCommunicator concurrentThreadMalCommunicator(ConcurrentUserListExtractor listExtractor,
                                                                    ConcurrentTitleMapper mapper) {
        SimilarAnimeCommunicator communicator = new SimilarAnimeCommunicator();
        communicator.setListExtractor(listExtractor);
        communicator.setTitleMapper(mapper);
        return communicator;
    }

    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
    @Qualifier("shiki")
    public SimilarAnimeCommunicator shikimoriCommunicator(ShikimoriListExtractor listExtractor,
                                                          ShikimoriTitleMapper mapper) {
        SimilarAnimeCommunicator communicator = new SimilarAnimeCommunicator();
        communicator.setListExtractor(listExtractor);
        communicator.setTitleMapper(mapper);
        return communicator;
    }
}

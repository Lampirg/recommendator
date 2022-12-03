package com.lampirg.recommendator.config;

import com.lampirg.recommendator.anidb.mal.MalCommunicator;
import com.lampirg.recommendator.anidb.mal.listextractor.ConcurrentUserListExtractor;
import com.lampirg.recommendator.anidb.mal.listextractor.SingleThreadUserListExtractor;
import com.lampirg.recommendator.anidb.mal.titlemapper.ConcurrentTitleMapper;
import com.lampirg.recommendator.anidb.mal.titlemapper.SingleThreadTitleMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScan("com.lampirg.recommendator.anidb")
public class MalConfiguration {

    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
    @Qualifier("mal-single")
    public MalCommunicator singleThreadMalCommunicator(SingleThreadUserListExtractor listExtractor,
                                                       SingleThreadTitleMapper mapper) {
        MalCommunicator communicator = new MalCommunicator();
        communicator.setListExtractor(listExtractor);
        communicator.setTitleMapper(mapper);
        return communicator;
    }

    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
    @Qualifier("mal-concurrent")
    public MalCommunicator concurrentThreadMalCommunicator(ConcurrentUserListExtractor listExtractor,
                                                           ConcurrentTitleMapper mapper) {
        MalCommunicator communicator = new MalCommunicator();
        communicator.setListExtractor(listExtractor);
        communicator.setTitleMapper(mapper);
        return communicator;
    }
}

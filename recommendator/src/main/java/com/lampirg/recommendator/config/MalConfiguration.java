package com.lampirg.recommendator.config;

import com.lampirg.recommendator.anidb.mal.MalCommunicator;
import com.lampirg.recommendator.anidb.mal.querymaker.ConcurrentUserListExtractor;
import com.lampirg.recommendator.anidb.mal.querymaker.SingleThreadUserListExtractor;
import com.lampirg.recommendator.anidb.mal.titlemapper.ConcurrentTitleMapper;
import com.lampirg.recommendator.anidb.mal.titlemapper.SingleThreadTitleMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScan("com.lampirg.recommendator.anidb")
public class MalConfiguration {

    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
    @Qualifier("single")
    public MalCommunicator singleThreadMalCommunicator(SingleThreadUserListExtractor queryMaker,
                                                       SingleThreadTitleMapper mapper) {
        MalCommunicator communicator = new MalCommunicator();
        communicator.setQueryMaker(queryMaker);
        communicator.setTitleMapper(mapper);
        return communicator;
    }

    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
    @Qualifier("concurrent")
    public MalCommunicator concurrentThreadMalCommunicator(ConcurrentUserListExtractor queryMaker,
                                                           ConcurrentTitleMapper mapper) {
        MalCommunicator communicator = new MalCommunicator();
        communicator.setQueryMaker(queryMaker);
        communicator.setTitleMapper(mapper);
        return communicator;
    }
}

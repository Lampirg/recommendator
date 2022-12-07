package com.lampirg.recommendator.config;

import com.lampirg.recommendator.anidb.general.SimilarAnimeCommunicator;
import com.lampirg.recommendator.anidb.mal.listextractor.ConcurrentUserListExtractor;
import com.lampirg.recommendator.anidb.mal.listextractor.SingleThreadUserListExtractor;
import com.lampirg.recommendator.anidb.mal.titlemapper.SingleThreadTitleMapper;
import com.lampirg.recommendator.anidb.shikimori.ShikimoriListExtractor;
import com.lampirg.recommendator.config.quilifiers.Shiki;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

@Configuration
public class ListExtractorConfiguration {

    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
    @Qualifier("mal-single")
    public SingleThreadUserListExtractor singleThreadUserListExtractor() {
        return new SingleThreadUserListExtractor();
    }

    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
    @Qualifier("mal-concurrent")
    public ConcurrentUserListExtractor concurrentUserListExtractor() {
        return new ConcurrentUserListExtractor();
    }

    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
    @Shiki
    public ShikimoriListExtractor shikimoriListExtractor() {
        return new ShikimoriListExtractor();
    }
}

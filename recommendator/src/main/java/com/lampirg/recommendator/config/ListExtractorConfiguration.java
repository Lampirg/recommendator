package com.lampirg.recommendator.config;

import com.lampirg.recommendator.anidb.specific.mal.listextractor.ConcurrentUserListExtractor;
import com.lampirg.recommendator.anidb.specific.mal.listextractor.SingleThreadUserListExtractor;
import com.lampirg.recommendator.anidb.specific.shikimori.ShikimoriListExtractor;
import com.lampirg.recommendator.config.qualifiers.Mal;
import com.lampirg.recommendator.config.qualifiers.Shiki;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

@Configuration
public class ListExtractorConfiguration {

    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
    @Mal("single")
    public SingleThreadUserListExtractor singleThreadUserListExtractor() {
        return new SingleThreadUserListExtractor();
    }

    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
    @Mal("concurrent")
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

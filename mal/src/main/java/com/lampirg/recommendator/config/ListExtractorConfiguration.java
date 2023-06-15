package com.lampirg.recommendator.config;

import com.lampirg.recommendator.anidb.listextractor.ConcurrentUserListExtractor;
import com.lampirg.recommendator.anidb.listextractor.SingleThreadUserListExtractor;
import com.lampirg.recommendator.config.qualifiers.Mal;
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

}

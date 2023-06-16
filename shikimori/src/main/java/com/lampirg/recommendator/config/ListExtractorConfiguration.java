package com.lampirg.recommendator.config;

import com.lampirg.recommendator.anidb.ShikimoriListExtractor;
import com.lampirg.recommendator.config.qualifiers.Shiki;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

@Configuration
public class ListExtractorConfiguration {

    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
    @Shiki
    public ShikimoriListExtractor shikimoriListExtractor() {
        return new ShikimoriListExtractor();
    }
}

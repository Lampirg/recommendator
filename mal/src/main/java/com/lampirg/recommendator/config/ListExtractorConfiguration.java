package com.lampirg.recommendator.config;

import com.lampirg.recommendator.anidb.listextractor.MalUserListExtractor;
import com.lampirg.recommendator.config.qualifiers.Mal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

@Configuration
public class ListExtractorConfiguration {

    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
    @Mal
    public MalUserListExtractor malUserListExtractor() {
        return new MalUserListExtractor();
    }
}

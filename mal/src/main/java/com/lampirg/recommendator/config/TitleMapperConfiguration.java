package com.lampirg.recommendator.config;

import com.lampirg.recommendator.anidb.titlemapper.ConcurrentTitleMapper;
import com.lampirg.recommendator.anidb.titlemapper.SingleThreadTitleMapper;
import com.lampirg.recommendator.config.qualifiers.Mal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

public class TitleMapperConfiguration {

    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
    @Mal("single")
    public SingleThreadTitleMapper singleThreadTitleMapper() {
        return new SingleThreadTitleMapper();
    }

    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
    @Mal("concurrent")
    public ConcurrentTitleMapper concurrentTitleMapper() {
        return new ConcurrentTitleMapper();
    }
}

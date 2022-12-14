package com.lampirg.recommendator.config;

import com.lampirg.recommendator.anidb.specific.mal.titlemapper.ConcurrentTitleMapper;
import com.lampirg.recommendator.anidb.specific.mal.titlemapper.SingleThreadTitleMapper;
import com.lampirg.recommendator.anidb.specific.shikimori.ShikimoriTitleMapper;
import com.lampirg.recommendator.config.qualifiers.Mal;
import com.lampirg.recommendator.config.qualifiers.Shiki;
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

    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
    @Shiki
    public ShikimoriTitleMapper shikimoriTitleMapper() {
        return new ShikimoriTitleMapper();
    }
}

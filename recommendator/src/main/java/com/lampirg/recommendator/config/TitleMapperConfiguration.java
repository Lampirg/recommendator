package com.lampirg.recommendator.config;

import com.lampirg.recommendator.anidb.mal.listextractor.SingleThreadUserListExtractor;
import com.lampirg.recommendator.anidb.mal.titlemapper.ConcurrentTitleMapper;
import com.lampirg.recommendator.anidb.mal.titlemapper.SingleThreadTitleMapper;
import com.lampirg.recommendator.anidb.shikimori.ShikimoriTitleMapper;
import com.lampirg.recommendator.config.quilifiers.Shiki;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

public class TitleMapperConfiguration {

    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
    @Qualifier("mal-single")
    public SingleThreadTitleMapper singleThreadTitleMapper() {
        return new SingleThreadTitleMapper();
    }

    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
    @Qualifier("mal-concurrent")
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

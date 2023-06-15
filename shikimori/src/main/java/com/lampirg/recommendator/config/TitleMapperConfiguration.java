package com.lampirg.recommendator.config;

import com.lampirg.recommendator.anidb.ShikimoriTitleMapper;
import com.lampirg.recommendator.config.qualifiers.Shiki;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

public class TitleMapperConfiguration {

    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
    @Shiki
    public ShikimoriTitleMapper shikimoriTitleMapper() {
        return new ShikimoriTitleMapper();
    }
}

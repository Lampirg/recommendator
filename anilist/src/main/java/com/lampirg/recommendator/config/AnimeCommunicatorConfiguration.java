package com.lampirg.recommendator.config;

import com.lampirg.recommendator.anidb.AnilistCommunicator;
import com.lampirg.recommendator.anidb.general.AnimeSiteCommunicator;
import com.lampirg.recommendator.config.qualifiers.Anilist;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.lampirg.recommendator.anidb")
public class AnimeCommunicatorConfiguration {

    @Bean
    @Anilist
    public AnimeSiteCommunicator anilistCommunicator() {
        return new AnilistCommunicator();
    }

}


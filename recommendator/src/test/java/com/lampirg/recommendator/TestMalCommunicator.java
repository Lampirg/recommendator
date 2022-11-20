package com.lampirg.recommendator;

import com.lampirg.recommendator.anidb.AnimeSiteCommunicator;
import com.lampirg.recommendator.model.UserAnimeTitle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.util.StopWatch;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@Profile("test")
public class TestMalCommunicator {

    private AnimeSiteCommunicator communicator;

    private static Logger logger = LogManager.getLogger(TestMalCommunicator.class);

    @Autowired
    public void setCommunicator(AnimeSiteCommunicator communicator) {
        this.communicator = communicator;
    }

    @Test
    void getRecommendationForRimisaki() {
        final int JUMPS = 1;
        Set<UserAnimeTitle> titles = communicator.getUserAnimeList("Rimisaki");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (int i = 0; i < JUMPS; i++) {
            communicator.getSimilarAnimeTitles(titles);
        }
        stopWatch.stop();
        logger.info(stopWatch.getTotalTimeSeconds() / JUMPS + " seconds");
    }

    @Test
    void getRecommendationForLampirg() {
        final int JUMPS = 1;
        Set<UserAnimeTitle> titles = communicator.getUserAnimeList("Lampirg");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (int i = 0; i < JUMPS; i++) {
            communicator.getSimilarAnimeTitles(titles);
        }
        stopWatch.stop();
        logger.info(stopWatch.getTotalTimeSeconds() / JUMPS + " seconds");
    }
}

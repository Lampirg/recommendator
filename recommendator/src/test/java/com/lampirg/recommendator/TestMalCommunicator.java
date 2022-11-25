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
public class TestMalCommunicator {

    private AnimeSiteCommunicator communicator;

    private static Logger logger = LogManager.getLogger(TestMalCommunicator.class);

    @Autowired
    public void setCommunicator(AnimeSiteCommunicator communicator) {
        this.communicator = communicator;
    }

    private void getRecommendationForUser(String user, int jumps) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (int i = 0; i < jumps; i++) {
            communicator.getSimilarAnimeTitles(user);
        }
        stopWatch.stop();
        logger.info(stopWatch.getTotalTimeSeconds() / jumps + " seconds");
    }

    @Test
    void getRecommendationForRimisaki() {
        getRecommendationForUser("Rimisaki", 1);
    }

    @Test
    void getRecommendationForLampirg() {
        getRecommendationForUser("Lampirg", 1);
    }

    @Test
    void getRecommendationForZerity() {
        getRecommendationForUser("Zerity", 1);
    }
}

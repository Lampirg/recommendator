package com.lampirg.recommendator;

import com.lampirg.recommendator.anidb.AnimeSiteCommunicator;
import com.lampirg.recommendator.model.AnimeRecommendation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StopWatch;

import java.util.Set;

@SpringBootTest
public class TestMalCommunicator {

    private ApplicationContext context;
    private AnimeSiteCommunicator singleThreadCommunicator;
    private AnimeSiteCommunicator concurrentThreadCommunicator;

    private static Logger logger = LogManager.getLogger(TestMalCommunicator.class);

    @Autowired
    public void setSingleThreadCommunicator(@Qualifier("single") AnimeSiteCommunicator communicator) {
        this.singleThreadCommunicator = communicator;
    }

    @Autowired
    public void setConcurrentThreadCommunicator(@Qualifier("concurrent") AnimeSiteCommunicator communicator) {
        this.concurrentThreadCommunicator = communicator;
    }

    private void getRecommendationForUser(AnimeSiteCommunicator communicator, String user, int jumps) {
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
        getRecommendationForUser(singleThreadCommunicator, "Rimisaki", 1);
        getRecommendationForUser(concurrentThreadCommunicator, "Rimisaki", 1);
    }

    @Test
    void getRecommendationForLampirg() {
        getRecommendationForUser(singleThreadCommunicator, "Lampirg", 1);
        getRecommendationForUser(concurrentThreadCommunicator, "Lampirg", 1);
    }

    @Test
    void getRecommendationForZerity() {
        getRecommendationForUser(singleThreadCommunicator, "Zerity", 1);
        getRecommendationForUser(concurrentThreadCommunicator, "Zerity", 1);
    }
}

package com.lampirg.speed;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.StopWatch;
// TODO: benchmark
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "PT90S")
public class BenchmarkMalCommunicator {

    private static Logger logger = LogManager.getLogger(TestMalCommunicator.class);

    WebTestClient webTestClient;

    @Autowired
    public void setWebTestClient(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    private void getRecommendationForUser(String user, int jumps) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        String uri = "/mal/"+user;
        for (int i = 0; i < jumps; i++) {
            webTestClient.get().uri(uri)
                    .exchange().expectStatus().is2xxSuccessful();
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

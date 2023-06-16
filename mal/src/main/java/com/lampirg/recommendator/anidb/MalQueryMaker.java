package com.lampirg.recommendator.anidb;

import com.lampirg.recommendator.anidb.general.QueryMaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Service
@PropertySource("classpath:mal security code.yml")
public class MalQueryMaker implements QueryMaker {

    private RPSQueryMaker queryMaker;

    @Value("${clientIdHeader}")
    private String clientIdHeader;
    @Value("${clientId}")
    private String clientId;

    private HttpEntity<String> request;

    @PostConstruct
    private void init() {
        HttpHeaders authHeader = new HttpHeaders();
        authHeader.set(clientIdHeader, clientId);
        this.request = new HttpEntity<>(authHeader);
    }

    @Autowired
    public void setQueryMaker(RPSQueryMaker queryMaker) {
        this.queryMaker = queryMaker;
    }

    @RateLimiter(name = "mal-rpm")
    @Retry(name = "rpm")
    public <T> ResponseEntity<T> exchange(String url, HttpMethod method, Class<T> responseType, Object... uriVariables) {
        return queryMaker.exchange(url, method, request, responseType, uriVariables);
    }

    @Service
    public static class RPSQueryMaker {

        private RestTemplate restTemplate;

        @Autowired
        public void setRestTemplate(RestTemplate restTemplate) {
            this.restTemplate = restTemplate;
        }

        @RateLimiter(name = "mal-rps")
        @Retry(name = "rps")
        public <T> ResponseEntity<T> exchange(String url, HttpMethod method, @Nullable HttpEntity<?> requestEntity, Class<T> responseType, Object... uriVariables) {
            return restTemplate.exchange(url, method, requestEntity, responseType, uriVariables);
        }
    }
}

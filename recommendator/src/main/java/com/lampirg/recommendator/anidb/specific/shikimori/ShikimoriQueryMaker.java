package com.lampirg.recommendator.anidb.specific.shikimori;

import com.lampirg.recommendator.anidb.general.QueryMaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Service
public class ShikimoriQueryMaker implements QueryMaker {
    private RPSQueryMaker queryMaker;
    private HttpEntity<String> request;

    @Autowired
    public void setQueryMaker(RPSQueryMaker queryMaker) {
        this.queryMaker = queryMaker;
    }

    @PostConstruct
    private void init() {
        this.request = new HttpEntity<>(new HttpHeaders());
    }

    @RateLimiter(name = "shikimori-rpm")
    @Retry(name = "rpm")
    public <T> ResponseEntity<T> exchange(String url, HttpMethod method, Class<T> responseType, Object... uriVariables) {
        return queryMaker.exchange(url, method, request, responseType, uriVariables);
    }

    @RateLimiter(name = "shikimori-rpm")
    @Retry(name = "rpm")
    public <T> ResponseEntity<T> exchange(String url, HttpMethod method, ParameterizedTypeReference<T> responseType, Object... uriVariables) throws RestClientException {
        return queryMaker.exchange(url, method, request, responseType, uriVariables);
    }

    @Service
    public static class RPSQueryMaker {

        private RestTemplate restTemplate;

        @Autowired
        public void setRestTemplate(RestTemplate restTemplate) {
            this.restTemplate = restTemplate;
        }

        @RateLimiter(name = "shikimori-rps")
        @Retry(name = "rps")
        public <T> ResponseEntity<T> exchange(String url, HttpMethod method, @Nullable HttpEntity<?> requestEntity, Class<T> responseType, Object... uriVariables) {
            return restTemplate.exchange(url, method, requestEntity, responseType, uriVariables);
        }

        @RateLimiter(name = "shikimori-rps")
        @Retry(name = "rps")
        public <T> ResponseEntity<T> exchange(String url, HttpMethod method, @Nullable HttpEntity<?> requestEntity, ParameterizedTypeReference<T> responseType, Object... uriVariables) throws RestClientException {
            return restTemplate.exchange(url, method, requestEntity, responseType, uriVariables);
        }
    }
}

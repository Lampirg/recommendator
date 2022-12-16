package com.lampirg.recommendator.anidb.specific.mal;

import com.lampirg.recommendator.anidb.general.SimilarAnimeCommunicator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
@PropertySource("classpath:mal security code.yml")
public class MalCommunicator extends SimilarAnimeCommunicator {

    @Value("${clientIdHeader}")
    private String clientIdHeader;
    @Value("${clientId}")
    private String clientId;

    @PostConstruct
    protected void init() {
        HttpHeaders authHeader = new HttpHeaders();
        authHeader.set(clientIdHeader, clientId);
        HttpEntity<String> request = new HttpEntity<>(authHeader);
        setRequest(request);
    }
}

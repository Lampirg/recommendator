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
public class MalCommunicator extends SimilarAnimeCommunicator {

}

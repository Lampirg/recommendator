package com.lampirg.recommendator.anidb.mal.querymaker;

import com.lampirg.recommendator.anidb.model.UserAnimeTitle;
import org.springframework.http.HttpEntity;

import java.util.Set;

public interface UserListExtractor {

    UserListExtractor setRequest(HttpEntity<String> request);
    void setUser(String username);
    Set<UserAnimeTitle> getToExclude();
    Set<UserAnimeTitle> getToInclude();
}

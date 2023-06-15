package com.lampirg.recommendator.anidb.general.listextractor;

import com.lampirg.recommendator.anidb.titles.model.UserAnimeTitle;
import org.springframework.http.HttpEntity;

import java.util.Set;

public interface UserListExtractor {
    void setUser(String username);
    Set<UserAnimeTitle> getToExclude();
    Set<UserAnimeTitle> getToInclude();
}

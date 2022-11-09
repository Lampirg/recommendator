package com.lampirg.recommendator.repository;

import com.lampirg.recommendator.model.AnimeTitle;

import java.util.ArrayList;
import java.util.List;

public class AnimeTitleRepository {

    List<AnimeTitle> titles = new ArrayList<>();

    public List<AnimeTitle> findAll() {
        return titles;
    }

    public AnimeTitle findById(long id) {
        return titles.stream().filter(stream -> stream.id() == id).findFirst().orElseThrow();
    }

    public AnimeTitle create(AnimeTitle stream) {
        titles.add(stream);
        return stream;
    }

    public void update(AnimeTitle title, long id) {
        AnimeTitle existing = titles.stream()
                .filter(s -> s.id() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("stream not found"));
        titles.set(titles.indexOf(existing), title);
    }

    public void delete(long id) {
        titles.removeIf(stream -> stream.id() == id);
    }
}

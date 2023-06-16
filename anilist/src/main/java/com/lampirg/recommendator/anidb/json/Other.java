package com.lampirg.recommendator.anidb.json;

import java.util.List;

public record Other(List<Lists> lists) {

    public record Lists(String status, List<Entries> entries) {
        public record Entries(Media media, int score) {
            public record Media(int id, Title title, CoverImage coverImage) {

                public record Title(String romaji) {}
                public record CoverImage(String extraLarge, String large, String medium) {}
            }
        }
    }
}

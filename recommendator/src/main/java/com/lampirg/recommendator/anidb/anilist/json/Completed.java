package com.lampirg.recommendator.anidb.anilist.json;

import java.util.List;

public record Completed(List<CompletedList> lists) {

    public record CompletedList(String status, List<Entries> entries) {
        public record Entries(Media media, int score) {
            public record Media(int id, Title title, CoverImage coverImage, Recommendations recommendations) {

                public record Title(String romaji) {}
                public record CoverImage(String extraLarge, String large, String medium) {}
                public record Recommendations(List<Nodes> nodes) {
                    public record Nodes(int rating, MediaRecommendation mediaRecommendation) {
                        public record MediaRecommendation(int id, Title title, CoverImage coverImage) {}
                    }
                }
            }
        }
    }
}

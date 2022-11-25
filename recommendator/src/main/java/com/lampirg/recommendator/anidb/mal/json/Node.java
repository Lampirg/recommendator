package com.lampirg.recommendator.anidb.mal.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Node(
        long id,
        String title,
        @JsonProperty("main_picture")
        MainPicture mainPicture
) {
        public Node {
                if (mainPicture == null)
                        mainPicture = new MainPicture("No picture", "No picture");
        }
}

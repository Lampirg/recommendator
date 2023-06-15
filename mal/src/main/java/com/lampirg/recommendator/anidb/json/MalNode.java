package com.lampirg.recommendator.anidb.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MalNode(
        long id,
        String title,
        @JsonProperty("main_picture")
        MainPicture mainPicture
) {
        public MalNode {
                if (mainPicture == null)
                        mainPicture = new MainPicture("No picture", "No picture");
        }
}

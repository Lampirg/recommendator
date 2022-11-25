package com.lampirg.recommendator.anidb.mal.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Data(
        Node node,
        @JsonProperty("list_status")
        ListStatus listStatus
) {
}

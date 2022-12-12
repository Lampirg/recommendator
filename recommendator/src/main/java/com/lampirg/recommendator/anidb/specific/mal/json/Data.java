package com.lampirg.recommendator.anidb.specific.mal.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Data(
        MalNode node,
        @JsonProperty("list_status")
        ListStatus listStatus
) {
}

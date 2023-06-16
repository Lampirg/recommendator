package com.lampirg.recommendator.anidb.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Data(
        MalNode node,
        @JsonProperty("list_status")
        ListStatus listStatus
) {
}

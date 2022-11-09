package com.lampirg.recommendator.mal;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Data(
        Node node,
        @JsonProperty("list_status")
        ListStatus listStatus
) {
}

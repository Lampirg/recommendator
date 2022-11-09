package com.lampirg.recommendator.mal;

import java.util.Objects;
import java.util.Optional;

public record MainPicture(
        String large,
        String medium
) {
    public String getLargeIfPresent() {
        return Objects.requireNonNull(Optional.ofNullable(large).orElse(medium));
    }
}

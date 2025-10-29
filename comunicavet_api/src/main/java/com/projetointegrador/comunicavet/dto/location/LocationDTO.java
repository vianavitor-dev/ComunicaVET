package com.projetointegrador.comunicavet.dto.location;

import jakarta.validation.constraints.NotNull;

public record LocationDTO(
        // latitude e longitude
        @NotNull String lat, @NotNull String lon
) {
}

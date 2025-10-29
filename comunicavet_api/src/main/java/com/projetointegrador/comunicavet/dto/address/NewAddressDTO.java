package com.projetointegrador.comunicavet.dto.address;

import com.projetointegrador.comunicavet.model.Location;
import jakarta.validation.constraints.NotNull;

/*
Uso: quando criando novo endereço;
 */
public record NewAddressDTO (
        @NotNull String city,
        @NotNull String state,
        @NotNull String country
) {
}

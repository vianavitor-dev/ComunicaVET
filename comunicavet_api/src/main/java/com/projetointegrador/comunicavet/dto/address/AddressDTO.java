package com.projetointegrador.comunicavet.dto.address;

import com.projetointegrador.comunicavet.model.Location;

/*
Uso: quando retornando valores; criando novo endereço; alterando;
 */
public record AddressDTO(
        Long id,
        String street,
        String city,
        String state,
        String country,
        int number,
        String neighborhood,
        Location location,
        String complement
) {
}

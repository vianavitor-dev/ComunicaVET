package com.projetointegrador.comunicavet.dto.address;

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
        double latitude,   // novo campo
        double longitude,  // novo campo
        String complement
) {
}
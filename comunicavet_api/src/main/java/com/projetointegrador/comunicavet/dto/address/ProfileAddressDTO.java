package com.projetointegrador.comunicavet.dto.address;

public record ProfileAddressDTO(
        String country,
        String state,
        String city,
        String neighborhood,
        String street,
        int number,
        String complement
) {
}

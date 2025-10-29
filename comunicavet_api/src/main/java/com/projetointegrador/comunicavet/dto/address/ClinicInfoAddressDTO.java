package com.projetointegrador.comunicavet.dto.address;

public record ClinicInfoAddressDTO (
        String city,
        String state,
        String neighborhood,
        String street,
        int number
) {
}

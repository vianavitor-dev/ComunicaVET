package com.projetointegrador.comunicavet.dto.clinic;

import com.projetointegrador.comunicavet.dto.contact.ContactDTO;

import java.util.List;

public record ClinicCardDTO(
        Long id,
        String name,
        List<ContactDTO> contacts,
        String countryName,
        String stateName,
        String cityName,
        String street,
        int stars,
        int viewsCount,
        boolean wasFavorited
) {
}

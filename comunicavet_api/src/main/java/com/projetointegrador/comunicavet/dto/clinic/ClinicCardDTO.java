package com.projetointegrador.comunicavet.dto.clinic;

public record ClinicCardDTO(
        Long id,
        String name,
        String phone, // ALTERADO: De List<ContactDTO> para String
        String countryName,
        String stateName,
        String cityName,
        String street,
        int stars,
        int viewsCount,
        boolean wasFavorited
) {
}
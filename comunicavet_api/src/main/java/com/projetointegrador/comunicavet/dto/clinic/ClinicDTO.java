package com.projetointegrador.comunicavet.dto.clinic;

import com.projetointegrador.comunicavet.dto.address.AddressDTO;

import java.time.LocalDate;
import java.util.List;

// Uso: quando retornando dados da Clínica; alterando;
public record ClinicDTO(
        Long id,
        String name,
        String email,
        AddressDTO address,
        int stars,
        String phone, // ALTERADO: De List<ContactDTO> para String
        String backgroundImagePath,
        String profileImagePath,
        LocalDate createdAt
) {
}
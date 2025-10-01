package com.projetointegrador.comunicavet.dto.clinic;

import com.projetointegrador.comunicavet.dto.address.AddressDTO;
import com.projetointegrador.comunicavet.dto.contact.ContactDTO;

import java.time.LocalDate;
import java.util.List;

// Uso: quando retornando dados da Clínica; alterando;
public record ClinicDTO(
        Long id,
        String name,
        String email,
        String password,
        AddressDTO address,
        int stars,
        List<ContactDTO> contacts,
        String backgroundImagePath,
        String profileImagePath,
        LocalDate createdAt
) {
}

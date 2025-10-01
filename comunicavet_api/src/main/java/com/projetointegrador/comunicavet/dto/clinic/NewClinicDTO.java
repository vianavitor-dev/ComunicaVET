package com.projetointegrador.comunicavet.dto.clinic;

import com.projetointegrador.comunicavet.dto.address.AddressDTO;
import com.projetointegrador.comunicavet.dto.contact.ContactDTO;

// Uso: quando criando uma nova Clinica
public record NewClinicDTO (
        String name,
        String email,
        String password,
        AddressDTO address,
        ContactDTO[] contacts,
        String backgroundImagePath,
        String profileImagePath
) {
}

package com.projetointegrador.comunicavet.dto.clinic;

import com.projetointegrador.comunicavet.dto.address.AddressDTO;

// Uso: quando criando uma nova Clinica
public record NewClinicDTO (
        String name,
        String email,
        String password,
        AddressDTO address,
        String phone, // ALTERADO: De ContactDTO[] para String
        String backgroundImagePath,
        String profileImagePath
) {
}
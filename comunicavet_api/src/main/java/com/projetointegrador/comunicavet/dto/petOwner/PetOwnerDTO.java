package com.projetointegrador.comunicavet.dto.petOwner;

import com.projetointegrador.comunicavet.dto.address.AddressDTO;

import java.time.LocalDate;

/*
Uso: quando retornando dados; alterando;
 */
public record PetOwnerDTO(
        Long id,
        String name,
        String email,
        AddressDTO address,
        String profileImagePath,
        LocalDate createdAt,
        LocalDate updatedAt
) {
}

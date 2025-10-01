package com.projetointegrador.comunicavet.dto.petOwner;

import com.projetointegrador.comunicavet.dto.address.AddressDTO;


// Uso: quando estiver criando um novo Dono de Pet
public record NewPetOwnerDTO (
        String name,
        String email,
        String password,
        AddressDTO address,
        String profileImagePath
) {
}

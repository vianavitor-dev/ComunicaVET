package com.projetointegrador.comunicavet.dto.petOwner;

import com.projetointegrador.comunicavet.dto.address.ProfileAddressDTO;

public record PetOwnerProfileDTO (
        String name, String email,
        ProfileAddressDTO address
) {
}

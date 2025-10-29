package com.projetointegrador.comunicavet.mapper;

import com.projetointegrador.comunicavet.dto.address.AddressDTO;
import com.projetointegrador.comunicavet.dto.address.ProfileAddressDTO;
import com.projetointegrador.comunicavet.dto.petOwner.NewPetOwnerDTO;
import com.projetointegrador.comunicavet.dto.petOwner.PetOwnerDTO;
import com.projetointegrador.comunicavet.dto.petOwner.PetOwnerProfileDTO;
import com.projetointegrador.comunicavet.model.Address;
import com.projetointegrador.comunicavet.model.PetOwner;

public class PetOwnerDTOMapper {
    public static PetOwner toPetOwner(NewPetOwnerDTO dto, Address address) {
        PetOwner p = new PetOwner();
        p.setName(dto.name());
        p.setEmail(dto.email());
        p.setPassword(dto.password());
        p.setAddress(address);
        p.setProfileImage(dto.profileImagePath());

        return p;
    }

    public static PetOwnerDTO toDto(PetOwner entity) {
        AddressDTO addressDTO = AddressDTOMapper.toAddressDto(entity.getAddress());

        return new PetOwnerDTO(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                addressDTO,
                entity.getProfileImage(),
                entity.getCreateAt(),
                entity.getUpdateAt()
        );
    }

    public static PetOwnerProfileDTO petOwnerProfileDto(PetOwner entity) {
        ProfileAddressDTO profileAddressDto = AddressDTOMapper.toProfileAddress(entity.getAddress());

        return new PetOwnerProfileDTO(
                entity.getName(), entity.getEmail(),
                profileAddressDto
        );
    }
}

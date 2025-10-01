package com.projetointegrador.comunicavet.mapper;

import com.projetointegrador.comunicavet.dto.focus.FocusDTO;
import com.projetointegrador.comunicavet.dto.petOwnerFocus.PetOwnerFocusDTO;
import com.projetointegrador.comunicavet.dto.petOwnerFocus.RequestPetOwnerFocusDTO;
import com.projetointegrador.comunicavet.model.PetOwnerFocus;

public class PetOwnerFocusDTOMapper {
    public static PetOwnerFocusDTO toPetOwnerFocusDto(PetOwnerFocus entity) {
        FocusDTO focusDto = FocusDTOMapper.toFocusDto(entity.getFocus());

        return new PetOwnerFocusDTO(
                entity.getId(), entity.getPetOwner().getId(), focusDto
        );
    }
}

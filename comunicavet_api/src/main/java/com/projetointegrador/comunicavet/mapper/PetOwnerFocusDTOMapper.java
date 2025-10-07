package com.projetointegrador.comunicavet.mapper;

import com.projetointegrador.comunicavet.dto.focus.FocusDTO;
import com.projetointegrador.comunicavet.dto.petOwnerFocus.NewPetOwnerFocusDTO;
import com.projetointegrador.comunicavet.dto.petOwnerFocus.PetOwnerFocusDTO;
import com.projetointegrador.comunicavet.model.Focus;
import com.projetointegrador.comunicavet.model.PetOwner;
import com.projetointegrador.comunicavet.model.PetOwnerFocus;
import jakarta.validation.constraints.NotNull;

public class PetOwnerFocusDTOMapper {
    public static PetOwnerFocusDTO toPetOwnerFocusDto(PetOwnerFocus entity) {
        FocusDTO focusDto = FocusDTOMapper.toFocusDto(entity.getFocus());

        return new PetOwnerFocusDTO(
                entity.getId(), entity.getPetOwner().getId(), focusDto
        );
    }

    public static PetOwnerFocus toPetOwnerFocus(Focus focus, PetOwner petOwner) {
        PetOwnerFocus petOwnerFocus = new PetOwnerFocus();

        petOwnerFocus.setFocus(focus);
        petOwnerFocus.setPetOwner(petOwner);

        return petOwnerFocus;
    }
}

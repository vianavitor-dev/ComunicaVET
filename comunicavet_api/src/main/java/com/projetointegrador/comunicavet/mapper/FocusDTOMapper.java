package com.projetointegrador.comunicavet.mapper;

import com.projetointegrador.comunicavet.dto.focus.FocusDTO;
import com.projetointegrador.comunicavet.model.Focus;

public class FocusDTOMapper {
    public static FocusDTO toFocusDto(Focus entity) {
       return new FocusDTO(entity.getName(), entity.getDescription());
    }
}

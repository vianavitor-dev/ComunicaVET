package com.projetointegrador.comunicavet.mapper;

import com.projetointegrador.comunicavet.dto.clinicFocus.ClinicFocusDTO;
import com.projetointegrador.comunicavet.dto.focus.FocusDTO;
import com.projetointegrador.comunicavet.model.ClinicFocus;

public class ClinicFocusDTOMapper {
    public static ClinicFocusDTO toClinicFocusDto(ClinicFocus entity) {
        FocusDTO focusDto = FocusDTOMapper.toFocusDto(entity.getFocus());

        return new ClinicFocusDTO(
                entity.getId(), entity.getClinic().getId(), focusDto
        );
    }
}

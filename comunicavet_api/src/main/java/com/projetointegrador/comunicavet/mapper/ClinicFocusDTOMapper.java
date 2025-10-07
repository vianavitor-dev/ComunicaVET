package com.projetointegrador.comunicavet.mapper;

import com.projetointegrador.comunicavet.dto.clinicFocus.ClinicFocusDTO;
import com.projetointegrador.comunicavet.dto.focus.FocusDTO;
import com.projetointegrador.comunicavet.model.Clinic;
import com.projetointegrador.comunicavet.model.ClinicFocus;
import com.projetointegrador.comunicavet.model.Focus;
import jakarta.validation.constraints.NotNull;

public class ClinicFocusDTOMapper {
    public static ClinicFocusDTO toClinicFocusDto(ClinicFocus entity) {
        FocusDTO focusDto = FocusDTOMapper.toFocusDto(entity.getFocus());

        return new ClinicFocusDTO(
                entity.getId(), entity.getClinic().getId(), focusDto
        );
    }

    public static ClinicFocus toClinicFocus(Clinic clinic, Focus focus) {
        ClinicFocus cf = new ClinicFocus();

        cf.setClinic(clinic);
        cf.setFocus(focus);

        return cf;
    }
}

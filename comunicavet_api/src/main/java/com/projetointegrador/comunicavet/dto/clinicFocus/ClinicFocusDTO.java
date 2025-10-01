package com.projetointegrador.comunicavet.dto.clinicFocus;

import com.projetointegrador.comunicavet.dto.clinic.ClinicDTO;
import com.projetointegrador.comunicavet.dto.focus.FocusDTO;

// Uso: quando retornando dados Foco da Clínica
public record ClinicFocusDTO(
        Long id,
        Long clinicId,
        FocusDTO focus
) {
}

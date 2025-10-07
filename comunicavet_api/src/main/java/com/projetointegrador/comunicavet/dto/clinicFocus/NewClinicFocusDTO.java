package com.projetointegrador.comunicavet.dto.clinicFocus;

// Uso: registrando Foco da Clínica
public record NewClinicFocusDTO(
        Long clinicId,
        Byte[] focusIds
) {
}

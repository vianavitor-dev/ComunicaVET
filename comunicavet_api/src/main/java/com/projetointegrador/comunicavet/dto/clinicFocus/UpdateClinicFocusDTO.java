package com.projetointegrador.comunicavet.dto.clinicFocus;

// Uso: alterando o Foco da Clínica
public record UpdateClinicFocusDTO (
        Long id,
        Long clinicId,
        Byte focusId
) {
}

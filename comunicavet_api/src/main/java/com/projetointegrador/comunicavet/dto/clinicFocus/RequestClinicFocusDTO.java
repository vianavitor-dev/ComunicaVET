package com.projetointegrador.comunicavet.dto.clinicFocus;

// Uso: criando novo Foco da Clinica; alterando
public record RequestClinicFocusDTO(
        Long id,
        Long clinicId,
        Byte focusId
) {
}

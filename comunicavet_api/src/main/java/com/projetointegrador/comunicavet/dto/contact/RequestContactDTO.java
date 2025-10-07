package com.projetointegrador.comunicavet.dto.contact;

// Uso: quando for criar um novo Contato para Clinica; alterar
public record RequestContactDTO(
        Long id,
        Long clinicId,
        Byte contactTypeId,
        String value
) {
}

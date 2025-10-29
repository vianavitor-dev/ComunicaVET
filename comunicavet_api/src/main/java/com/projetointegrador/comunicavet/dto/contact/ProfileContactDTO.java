package com.projetointegrador.comunicavet.dto.contact;

public record ProfileContactDTO (
        Long id, // pode ser null se for um novo contato
        String contactTypeName,
        String value
) {
}

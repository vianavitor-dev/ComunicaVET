package com.projetointegrador.comunicavet.dto.petOwnerFocus;

// Uso: quando criando um novo Dono de Pet + Foco; alterando
public record RequestPetOwnerFocusDTO(
        Long id,
        Long petOwnerId,
        Long focusId
) {
}

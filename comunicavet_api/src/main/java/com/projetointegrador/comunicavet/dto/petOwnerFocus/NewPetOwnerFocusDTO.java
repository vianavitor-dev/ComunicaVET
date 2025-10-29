package com.projetointegrador.comunicavet.dto.petOwnerFocus;

// Uso: quando criando um novo Dono de Pet + Foco
public record NewPetOwnerFocusDTO(
        Long petOwnerId,
        String[] focusNames
) {
}

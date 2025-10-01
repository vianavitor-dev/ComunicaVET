package com.projetointegrador.comunicavet.dto.petOwnerFocus;

import com.projetointegrador.comunicavet.dto.focus.FocusDTO;

// Uso: quando retornando dados do Dono do Pet + Foco;
public record PetOwnerFocusDTO (
        Long id,
        Long petOwnerId,
        FocusDTO focus
) {
}

package com.projetointegrador.comunicavet.dto.favoriteClinic;

import com.projetointegrador.comunicavet.dto.clinic.ClinicCardDTO;
import com.projetointegrador.comunicavet.dto.clinic.ClinicDTO;

// Uso: quando retornando dados da Clinica Favorita
public record FavoriteClinicDTO (
        Long id,
        Long petOwnerId,
        ClinicCardDTO clinic
) {
}

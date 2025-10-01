package com.projetointegrador.comunicavet.dto.favoriteClinic;

// Uso: quando criando uma nova Clinica Favorita; alterando;
public record RequestFavoriteClinicDTO(
        Long id,
        Long petOwnerId,
        Long clinicId
) {
}

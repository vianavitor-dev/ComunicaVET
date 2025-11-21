package com.projetointegrador.comunicavet.dto.pet;

import com.projetointegrador.comunicavet.model.PetSize;
import com.projetointegrador.comunicavet.model.PetType;

import java.time.LocalDate;

public record PetDTO(
        Long id,
        String name,
        PetType type,
        String breed,
        String secondBreed,
        PetSize size,
        LocalDate dateBirth,
        Long ownerId
) {
}

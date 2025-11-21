package com.projetointegrador.comunicavet.dto.petClinicHistory;

import com.projetointegrador.comunicavet.dto.clinic.ClinicCardDTO;
import com.projetointegrador.comunicavet.dto.clinic.ClinicDTO;

import java.time.LocalDate;
import java.time.LocalTime;

public record PetClinicRegisterDTO(
        Long id,
        LocalDate registeredAt,
        ClinicCardDTO clinicCardDTO
) {
}

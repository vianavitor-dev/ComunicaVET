package com.projetointegrador.comunicavet.dto.petClinicHistory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.projetointegrador.comunicavet.dto.clinic.ClinicCardDTO;
import com.projetointegrador.comunicavet.dto.clinic.ClinicDTO;
import com.projetointegrador.comunicavet.dto.clinic.ClinicHistoryCardDTO;

import java.time.LocalDate;
import java.time.LocalTime;

public record PetClinicRegisterDTO(
        Long id,
        LocalDate registeredAt,
        ClinicHistoryCardDTO registeredClinic
) {
}

package com.projetointegrador.comunicavet.mapper;

import com.projetointegrador.comunicavet.dto.petClinicHistory.PetClinicRegisterDTO;
import com.projetointegrador.comunicavet.model.PetClinicHistory;

public class PetClinicHistoryDTOMapper {

    public static PetClinicRegisterDTO toPetClinicRegisterDTO(PetClinicHistory history) {
        var clinicHistoryCardDTO = ClinicDTOMapper.toClinicHistoryCardDTO(history.getClinic());

        return new PetClinicRegisterDTO(
                history.getId(),
                history.getRegisteredAt(),
                clinicHistoryCardDTO
        );
    }
}

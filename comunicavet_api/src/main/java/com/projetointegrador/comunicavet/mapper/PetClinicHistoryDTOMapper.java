package com.projetointegrador.comunicavet.mapper;

import com.projetointegrador.comunicavet.dto.petClinicHistory.PetClinicRegisterDTO;
import com.projetointegrador.comunicavet.model.PetClinicHistory;

public class PetClinicHistoryDTOMapper {

    public static PetClinicRegisterDTO toPetClinicRegisterDTO(PetClinicHistory history, boolean wasFavorited) {
        var clinicCardDto = ClinicDTOMapper.clinicCardDTO(history.getClinic(), wasFavorited);

        return new PetClinicRegisterDTO(
                history.getId(),
                history.getRegisteredAt(),
                clinicCardDto
        );
    }
}

package com.projetointegrador.comunicavet.mapper;

import com.projetointegrador.comunicavet.dto.petClinicHistory.PetClinicRegisterDTO;
import com.projetointegrador.comunicavet.model.Contact;
import com.projetointegrador.comunicavet.model.PetClinicHistory;

public class PetClinicHistoryDTOMapper {

    public static PetClinicRegisterDTO toPetClinicRegisterDTO(PetClinicHistory history, Contact[] clinicContacts, boolean wasFavorited) {
        var clinicCardDto = ClinicDTOMapper.clinicCardDTO(history.getClinic(), clinicContacts, wasFavorited);

        return new PetClinicRegisterDTO(
                history.getId(),
                history.getRegisteredAt(),
                clinicCardDto
        );
    }
}

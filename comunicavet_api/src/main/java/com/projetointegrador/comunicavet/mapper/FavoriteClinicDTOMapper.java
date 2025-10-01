package com.projetointegrador.comunicavet.mapper;

import com.projetointegrador.comunicavet.dto.clinic.ClinicDTO;
import com.projetointegrador.comunicavet.dto.contact.ContactDTO;
import com.projetointegrador.comunicavet.dto.favoriteClinic.FavoriteClinicDTO;
import com.projetointegrador.comunicavet.dto.favoriteClinic.RequestFavoriteClinicDTO;
import com.projetointegrador.comunicavet.model.Contact;
import com.projetointegrador.comunicavet.model.FavoriteClinic;

public class FavoriteClinicDTOMapper {
    public static FavoriteClinicDTO toFavoriteClinicDTO(FavoriteClinic entity, Contact[] clinicContacts) {
        ClinicDTO clinicDto = ClinicDTOMapper.toClinicDto(
                entity.getClinic(),
                clinicContacts
        );

        return new FavoriteClinicDTO(
                entity.getId(), entity.getPetOwner().getId(),
                clinicDto
        );
    }
}

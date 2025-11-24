package com.projetointegrador.comunicavet.mapper;

import com.projetointegrador.comunicavet.dto.clinic.ClinicCardDTO;
import com.projetointegrador.comunicavet.dto.favoriteClinic.FavoriteClinicDTO;
import com.projetointegrador.comunicavet.model.Clinic;
import com.projetointegrador.comunicavet.model.FavoriteClinic;
import com.projetointegrador.comunicavet.model.PetOwner;

public class FavoriteClinicDTOMapper {
    public static FavoriteClinicDTO toFavoriteClinicDTO(FavoriteClinic entity) {
        // ALTERADO: Remove o parâmetro Contact[] e usa o método clinicCardDTO atualizado
        ClinicCardDTO clinicCardDto = ClinicDTOMapper.clinicCardDTO(
                entity.getClinic(), true
        );

        return new FavoriteClinicDTO(
                entity.getId(), entity.getPetOwner().getId(),
                clinicCardDto
        );
    }

    public static FavoriteClinic toFavoriteClinic(Clinic clinic, PetOwner petOwner) {
        FavoriteClinic favoriteClinic = new FavoriteClinic();
        favoriteClinic.setClinic(clinic);
        favoriteClinic.setPetOwner(petOwner);

        return favoriteClinic;
    }
}
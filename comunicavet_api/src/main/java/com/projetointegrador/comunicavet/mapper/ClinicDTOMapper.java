package com.projetointegrador.comunicavet.mapper;

import com.projetointegrador.comunicavet.dto.address.AddressDTO;
import com.projetointegrador.comunicavet.dto.address.ClinicInfoAddressDTO;
import com.projetointegrador.comunicavet.dto.address.ProfileAddressDTO;
import com.projetointegrador.comunicavet.dto.clinic.*;
import com.projetointegrador.comunicavet.dto.focus.FocusDTO;
import com.projetointegrador.comunicavet.model.Address;
import com.projetointegrador.comunicavet.model.Clinic;

import java.util.List;

public class ClinicDTOMapper {

    public static Clinic toClinic(NewClinicDTO dto, Address address) {
        Clinic c = new Clinic();

        c.setName(dto.name());
        c.setEmail(dto.email());
        c.setPassword(dto.password());
        c.setAddress(address);
        c.setPhone(dto.phone()); // NOVO: definir telefone
        c.setBackgroundImage(dto.backgroundImagePath());
        c.setProfileImage(dto.profileImagePath());

        return c;
    }

    public static ClinicDTO toClinicDto(Clinic entity) {
        AddressDTO addressDTO = AddressDTOMapper.toAddressDto(entity.getAddress());

        return new ClinicDTO(
                entity.getId(), entity.getName(), entity.getEmail(),
                addressDTO, entity.getStars(), entity.getPhone(), // ALTERADO: entity.getPhone()
                entity.getBackgroundImage(), entity.getProfileImage(), entity.getCreateAt()
        );
    }

    public static ClinicCardDTO clinicCardDTO(Clinic entity, boolean wasFavorited) {
        AddressDTO addressDTO = AddressDTOMapper.toAddressDto(entity.getAddress());

        return new ClinicCardDTO(
                entity.getId(),
                entity.getName(), entity.getPhone(), // ALTERADO: entity.getPhone()
                addressDTO.country(), addressDTO.state(), addressDTO.city(),
                addressDTO.street(), entity.getStars(), entity.getViewers(), wasFavorited
        );
    }

    public static ClinicProfileDTO toClinicProfileDto(Clinic entity, List<FocusDTO> focuses) {
        ProfileAddressDTO profileAddressDto = AddressDTOMapper.toProfileAddress(entity.getAddress());

        return new ClinicProfileDTO(
                entity.getName(), entity.getEmail(),
                entity.getDescription(),
                profileAddressDto,
                entity.getPhone(), // ALTERADO: entity.getPhone()
                focuses
        );
    }

    public static ClinicInfoDTO toClinicInfoDTO(Clinic entity, List<FocusDTO> focuses, boolean wasFavorited) {
        ClinicInfoAddressDTO infoAddressDto = AddressDTOMapper.clinicInfoAddressDTO(entity.getAddress());

        return new ClinicInfoDTO(
                entity.getName(), entity.getEmail(),
                entity.getDescription(),
                entity.getStars(),
                infoAddressDto,
                entity.getPhone(), // ALTERADO: entity.getPhone()
                focuses,
                wasFavorited
        );
    }

    public static ClinicHistoryCardDTO toClinicHistoryCardDTO(Clinic entity) {
        return new ClinicHistoryCardDTO(
                entity.getId(), entity.getName()
        );
    }
}
package com.projetointegrador.comunicavet.mapper;

import com.projetointegrador.comunicavet.dto.address.AddressDTO;
import com.projetointegrador.comunicavet.dto.address.ClinicInfoAddressDTO;
import com.projetointegrador.comunicavet.dto.address.ProfileAddressDTO;
import com.projetointegrador.comunicavet.dto.clinic.*;
import com.projetointegrador.comunicavet.dto.contact.ContactDTO;
import com.projetointegrador.comunicavet.dto.contact.ProfileContactDTO;
import com.projetointegrador.comunicavet.dto.focus.FocusDTO;
import com.projetointegrador.comunicavet.model.Address;
import com.projetointegrador.comunicavet.model.Clinic;
import com.projetointegrador.comunicavet.model.Contact;
import com.projetointegrador.comunicavet.model.Focus;

import java.util.Arrays;
import java.util.List;

public class ClinicDTOMapper {

    public static Clinic toClinic(NewClinicDTO dto, Address address) {
        Clinic c = new Clinic();

        c.setName(dto.name());
        c.setEmail(dto.email());
        c.setPassword(dto.password());
        c.setAddress(address);
        c.setBackgroundImage(dto.backgroundImagePath());
        c.setProfileImage(dto.profileImagePath());

        return c;
    }

    public static ClinicDTO toClinicDto(Clinic entity, Contact[] clinicContacts) {
        AddressDTO addressDTO = AddressDTOMapper.toAddressDto(entity.getAddress());

        // Transforma o array de contatos (do tipo Contact) em uma lista de contatos Dto (ContactDTO)
        List<ContactDTO> contacts = Arrays.stream(clinicContacts)
                .map(ContactDTOMapper::toContactDto).toList();

        return new ClinicDTO(
                entity.getId(), entity.getName(), entity.getEmail(),
                addressDTO, entity.getStars(), contacts, entity.getBackgroundImage(),
                entity.getProfileImage(), entity.getCreateAt()
        );
    }

    public static ClinicCardDTO clinicCardDTO(Clinic entity, Contact[] clinicContacts, boolean wasFavorited) {
        AddressDTO addressDTO = AddressDTOMapper.toAddressDto(entity.getAddress());

        // Transforma o array de contatos (do tipo Contact) em uma lista de contatos Dto (ContactDTO)
        List<ContactDTO> contacts = Arrays.stream(clinicContacts)
                .map(ContactDTOMapper::toContactDto).toList();

        return new ClinicCardDTO(
                entity.getId(),
                entity.getName(), contacts,
                addressDTO.country(), addressDTO.state(), addressDTO.city(),
                addressDTO.street(), entity.getStars(), entity.getViewers(), wasFavorited
        );
    }

    public static ClinicProfileDTO toClinicProfileDto
            (Clinic entity, List<Contact> clinicContacts, List<FocusDTO> focuses) {

        ProfileAddressDTO profileAddressDto = AddressDTOMapper.toProfileAddress(entity.getAddress());

        List<ProfileContactDTO> contacts = clinicContacts.stream()
                .map(ContactDTOMapper::toProfileContactDto)
                .toList();

        return new ClinicProfileDTO(
                entity.getName(), entity.getEmail(),
                entity.getDescription(),
                profileAddressDto,
                contacts,
                focuses
        );
    }

    public static ClinicInfoDTO toClinicInfoDTO
            (Clinic entity, List<Contact> clinicContacts, List<FocusDTO> focuses, boolean wasFavorited) {

        ClinicInfoAddressDTO infoAddressDto = AddressDTOMapper.clinicInfoAddressDTO(entity.getAddress());

        List<ProfileContactDTO> contacts = clinicContacts.stream()
                .map(ContactDTOMapper::toProfileContactDto)
                .toList();

        return new ClinicInfoDTO(
                entity.getName(), entity.getEmail(),
                entity.getDescription(),
                entity.getStars(),
                infoAddressDto,
                contacts,
                focuses,
                wasFavorited
        );
    }
}

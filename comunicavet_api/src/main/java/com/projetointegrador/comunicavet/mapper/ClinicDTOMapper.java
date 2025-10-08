package com.projetointegrador.comunicavet.mapper;

import com.projetointegrador.comunicavet.dto.address.AddressDTO;
import com.projetointegrador.comunicavet.dto.clinic.ClinicDTO;
import com.projetointegrador.comunicavet.dto.clinic.NewClinicDTO;
import com.projetointegrador.comunicavet.dto.contact.ContactDTO;
import com.projetointegrador.comunicavet.model.Address;
import com.projetointegrador.comunicavet.model.Clinic;
import com.projetointegrador.comunicavet.model.Contact;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClinicDTOMapper {
    public static Clinic toClinic(ClinicDTO dto, Address address) {
        Clinic c = new Clinic();

        c.setId(dto.id());
        c.setName(dto.name());
        c.setEmail(dto.email());
//        c.setPassword(dto.password());
        c.setAddress(address);
        c.setBackgroundImage(dto.backgroundImagePath());
        c.setProfileImage(dto.profileImagePath());
        c.setStars(dto.stars());
        c.setCreateAt(dto.createdAt());

        return c;
    }

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
        AddressDTO addressDTO = AddressDTOMapper.toDto(entity.getAddress());
        List<ContactDTO> contacts = new ArrayList<>();

        // Transforma o array de contatos (do tipo Contact) em uma lista de contatos Dto (ContactDTO)
        Arrays.stream(clinicContacts).forEach(contact -> {
            contacts.add(ContactDTOMapper.toContactDto(contact));
        });

        return new ClinicDTO(
                entity.getId(), entity.getName(), entity.getEmail(),
                addressDTO, entity.getStars(), contacts, entity.getBackgroundImage(),
                entity.getProfileImage(), entity.getCreateAt()
        );
    }
}

package com.projetointegrador.comunicavet.mapper;

import com.projetointegrador.comunicavet.dto.contact.ContactDTO;
import com.projetointegrador.comunicavet.dto.contact.ProfileContactDTO;
import com.projetointegrador.comunicavet.dto.contact.RequestContactDTO;
import com.projetointegrador.comunicavet.model.Clinic;
import com.projetointegrador.comunicavet.model.Contact;
import com.projetointegrador.comunicavet.model.ContactType;

public class ContactDTOMapper {
    public static Contact toContact(RequestContactDTO dto, ContactType type, Clinic clinic) {
        Contact c = new Contact();
        c.setId(dto.id());
        c.setType(type);
        c.setClinic(clinic);
        c.setValue(dto.value());

        return c;
    }

    public static ContactDTO toContactDto(Contact entity) {
        return new ContactDTO(
                entity.getType().getName(), entity.getValue()
        );
    }

    public static ProfileContactDTO toProfileContactDto(Contact entity) {
        return new ProfileContactDTO(
                entity.getId(),
                entity.getType().getName(), entity.getValue()
        );
    }
}

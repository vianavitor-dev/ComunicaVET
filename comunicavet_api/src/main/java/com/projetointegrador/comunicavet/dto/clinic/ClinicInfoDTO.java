package com.projetointegrador.comunicavet.dto.clinic;

import com.projetointegrador.comunicavet.dto.address.ClinicInfoAddressDTO;
import com.projetointegrador.comunicavet.dto.contact.ProfileContactDTO;
import com.projetointegrador.comunicavet.dto.focus.FocusDTO;

import java.util.List;

public record ClinicInfoDTO (
        String name, String email,
        String description,
        int stars,
        ClinicInfoAddressDTO address,
        List<ProfileContactDTO> contacts,
        List<FocusDTO> focuses,
        boolean wasFavorited
) {
}

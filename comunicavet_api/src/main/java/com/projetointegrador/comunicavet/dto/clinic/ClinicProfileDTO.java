package com.projetointegrador.comunicavet.dto.clinic;

import com.projetointegrador.comunicavet.dto.address.ProfileAddressDTO;
import com.projetointegrador.comunicavet.dto.contact.ProfileContactDTO;
import com.projetointegrador.comunicavet.dto.focus.FocusDTO;

import java.util.List;

public record ClinicProfileDTO (
    String name, String email,
    String description,
    ProfileAddressDTO address,
    List<ProfileContactDTO> contacts,
    List<FocusDTO> focuses
) {}


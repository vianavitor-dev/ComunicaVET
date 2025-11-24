package com.projetointegrador.comunicavet.dto.clinic;

import com.projetointegrador.comunicavet.dto.address.ProfileAddressDTO;
import com.projetointegrador.comunicavet.dto.focus.FocusDTO;

import java.util.List;

public record ClinicProfileDTO (
        String name, String email,
        String description,
        ProfileAddressDTO address,
        String phone, // ALTERADO: De List<ProfileContactDTO> para String
        List<FocusDTO> focuses
) {}
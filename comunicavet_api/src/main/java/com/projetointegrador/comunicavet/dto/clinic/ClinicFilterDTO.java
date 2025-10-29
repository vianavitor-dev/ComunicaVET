package com.projetointegrador.comunicavet.dto.clinic;

import com.projetointegrador.comunicavet.dto.address.NewAddressDTO;
import com.projetointegrador.comunicavet.dto.location.SearchLocationDTO;

import java.util.List;

public record ClinicFilterDTO(
        Long userId,
        List<String> tagNames,
        SearchLocationDTO newAddressDto
) {
}

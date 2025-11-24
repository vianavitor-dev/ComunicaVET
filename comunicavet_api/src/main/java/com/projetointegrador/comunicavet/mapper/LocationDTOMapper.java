package com.projetointegrador.comunicavet.mapper;

import com.projetointegrador.comunicavet.dto.location.LocationDTO;
import com.projetointegrador.comunicavet.dto.location.SearchLocationDTO;
import com.projetointegrador.comunicavet.model.Address;

public class LocationDTOMapper {
    public static SearchLocationDTO toSearchLocationDTO(Address address) {
        return new SearchLocationDTO(
                null, address.getStreet(), address.getCity().getName(),
                address.getState().getName(), null, address.getCountry().getName(),
                null
        );
    }

}

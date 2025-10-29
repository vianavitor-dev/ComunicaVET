package com.projetointegrador.comunicavet.mapper;

import com.projetointegrador.comunicavet.dto.location.LocationDTO;
import com.projetointegrador.comunicavet.dto.location.SearchLocationDTO;
import com.projetointegrador.comunicavet.model.Address;
import com.projetointegrador.comunicavet.model.Location;

public class LocationDTOMapper {
    public static Location toLocation(LocationDTO dto) {
        Location location = new Location();

        location.setLatitude(Double.parseDouble(dto.lat()));
        location.setLongitude(Double.parseDouble(dto.lon()));

        return location;
    }

    public static SearchLocationDTO toSearchLocationDTO(Address address) {
        return new SearchLocationDTO(
                null, address.getStreet(), address.getCity().getName(),
                address.getState().getName(), null, address.getCountry().getName(),
                null
        );
    }

}

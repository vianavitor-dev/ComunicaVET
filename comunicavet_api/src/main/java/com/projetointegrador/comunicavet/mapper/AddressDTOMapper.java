package com.projetointegrador.comunicavet.mapper;

import com.projetointegrador.comunicavet.dto.address.AddressDTO;
import com.projetointegrador.comunicavet.model.Address;
import com.projetointegrador.comunicavet.model.City;
import com.projetointegrador.comunicavet.model.Country;
import com.projetointegrador.comunicavet.model.State;

public class AddressDTOMapper {
    public static Address toAddress(AddressDTO dto, Country country, State state, City city) {
        Address addr = new Address();
        addr.setId(dto.id());
        addr.setLocation(dto.location());
        addr.setCountry(country);
        addr.setCity(city);
        addr.setStreet(dto.street());
        addr.setNumber(dto.number());
        addr.setNeighborhood(dto.neighborhood());
        addr.setComplement(dto.complement());
        addr.setState(state);

        return addr;
    }

    public static AddressDTO toDto(Address entity) {
        return new AddressDTO(
                entity.getId(),
                entity.getStreet(), entity.getCity().getName(), entity.getState().getName(),
                entity.getCountry().getName(), entity.getNumber(), entity.getNeighborhood(),
                entity.getLocation(), entity.getComplement()
        );
    }
}

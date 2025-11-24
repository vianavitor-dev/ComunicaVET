package com.projetointegrador.comunicavet.mapper;

import com.projetointegrador.comunicavet.dto.address.AddressDTO;
import com.projetointegrador.comunicavet.dto.address.ClinicInfoAddressDTO;
import com.projetointegrador.comunicavet.dto.address.NewAddressDTO;
import com.projetointegrador.comunicavet.dto.address.ProfileAddressDTO;
import com.projetointegrador.comunicavet.model.Address;
import com.projetointegrador.comunicavet.model.City;
import com.projetointegrador.comunicavet.model.Country;
import com.projetointegrador.comunicavet.model.State;

public class AddressDTOMapper {
    public static Address toAddress(AddressDTO dto, Country country, State state, City city) {
        Address addr = new Address();
        addr.setId(dto.id());
        addr.setLatitude(dto.latitude());   // novo
        addr.setLongitude(dto.longitude()); // novo
        addr.setCountry(country);
        addr.setCity(city);
        addr.setStreet(dto.street());
        addr.setNumber(dto.number());
        addr.setNeighborhood(dto.neighborhood());
        addr.setComplement(dto.complement());
        addr.setState(state);

        return addr;
    }

    public static AddressDTO toAddressDto(Address entity) {
        return new AddressDTO(
                entity.getId(),
                entity.getStreet(), entity.getCity().getName(), entity.getState().getName(),
                entity.getCountry().getName(), entity.getNumber(), entity.getNeighborhood(),
                entity.getLatitude(), entity.getLongitude(), // novos campos
                entity.getComplement()
        );
    }

    public static NewAddressDTO toNewAddressDto(Address entity) {
        return new NewAddressDTO(
                entity.getCity().getName(), entity.getState().getName(), entity.getCountry().getName()
        );
    }

    public static ProfileAddressDTO toProfileAddress(Address entity) {
        return new ProfileAddressDTO(
                entity.getCountry().getName(),
                entity.getState().getName(),
                entity.getCity().getName(),
                entity.getNeighborhood(), entity.getStreet(),
                entity.getNumber(), entity.getComplement()
        );
    }

    public static ClinicInfoAddressDTO clinicInfoAddressDTO(Address entity) {
        return new ClinicInfoAddressDTO(
                entity.getCity().getName(), entity.getState().getName(),
                entity.getNeighborhood(), entity.getStreet(), entity.getNumber()
        );
    }
}

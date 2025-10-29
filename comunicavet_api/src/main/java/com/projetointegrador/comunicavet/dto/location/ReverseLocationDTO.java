package com.projetointegrador.comunicavet.dto.location;

public record ReverseLocationDTO (
        String type,
        LocationAddress address
) {
    public record LocationAddress(
            String amenity,
            Integer house_number,
            String road,
            String neighbourhood,
            String city_district,
            String city,
            String state,
            String postcode,
            String country,
            String country_code
    ) {}
}

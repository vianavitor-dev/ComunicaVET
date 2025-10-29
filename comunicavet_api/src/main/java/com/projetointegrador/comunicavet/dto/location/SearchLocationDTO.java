package com.projetointegrador.comunicavet.dto.location;

public record SearchLocationDTO(
        /*
            Amenity:
             nome de um ponto de interesse, e/ou o tipo de local,
             por exemplo: restaurante, escola, faculdade, etc...
         */
        String amenity,
        // nome da rua e número da casa (separar por vírgula)
        String street,
        String city,
        String state,
        String county,
        String country,
        Integer postalCode
) {
}

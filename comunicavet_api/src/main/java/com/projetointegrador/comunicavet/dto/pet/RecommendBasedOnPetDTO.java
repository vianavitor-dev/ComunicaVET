package com.projetointegrador.comunicavet.dto.pet;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.projetointegrador.comunicavet.model.PetSize;
import com.projetointegrador.comunicavet.model.PetType;

public record RecommendBasedOnPetDTO (
    @JsonProperty("pet_type") PetType type,
    @JsonProperty("pet_size") PetSize size,
    @JsonProperty("pet_age_months") int ageInMonths,
    @JsonProperty("pet_breed") String breed,
    @JsonProperty("pet_name") String name
) {
}

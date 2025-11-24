package com.projetointegrador.comunicavet.repository;

import com.projetointegrador.comunicavet.model.Pet;
import com.projetointegrador.comunicavet.model.PetOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findByPetOwner(PetOwner petOwner);
}

package com.projetointegrador.comunicavet.repository;

import com.projetointegrador.comunicavet.model.Clinic;
import com.projetointegrador.comunicavet.model.FavoriteClinic;
import com.projetointegrador.comunicavet.model.PetOwner;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteClinicRepository extends CrudRepository<FavoriteClinic, Long> {
    List<FavoriteClinic> findByClinic(Clinic clinic);

    List<FavoriteClinic> findByPetOwner(PetOwner petOwner);
}

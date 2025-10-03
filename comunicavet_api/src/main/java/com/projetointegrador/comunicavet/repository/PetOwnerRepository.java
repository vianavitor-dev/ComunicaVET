package com.projetointegrador.comunicavet.repository;

import com.projetointegrador.comunicavet.model.PetOwner;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PetOwnerRepository extends CrudRepository<PetOwner, Long> {
    List<PetOwner> findByName(String name);
    Optional<PetOwner> findByEmail(String email);
}


package com.projetointegrador.comunicavet.repository;

import com.projetointegrador.comunicavet.model.PetOwner;
import com.projetointegrador.comunicavet.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PetOwnerRepository extends CrudRepository<PetOwner, Long> {

}


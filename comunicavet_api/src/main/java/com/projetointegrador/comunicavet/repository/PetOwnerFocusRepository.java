package com.projetointegrador.comunicavet.repository;

import com.projetointegrador.comunicavet.model.Focus;
import com.projetointegrador.comunicavet.model.PetOwner;
import com.projetointegrador.comunicavet.model.PetOwnerFocus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetOwnerFocusRepository extends CrudRepository<PetOwnerFocus, Long> {
    List<PetOwnerFocus> findByPetOwner(PetOwner petOwner);

    List<PetOwnerFocus> findByFocus(Focus focus);
}

package com.projetointegrador.comunicavet.repository;

import com.projetointegrador.comunicavet.model.PetClinicHistory;
import com.projetointegrador.comunicavet.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetClinicHistoryRepository extends JpaRepository<PetClinicHistory, Long> {
    List<PetClinicHistory> findByPet(Pet pet);
}

package com.projetointegrador.comunicavet.repository;

import com.projetointegrador.comunicavet.model.Clinic;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClinicRepository extends CrudRepository<Clinic, Long> {
}

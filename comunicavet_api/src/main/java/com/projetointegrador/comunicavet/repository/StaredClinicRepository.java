package com.projetointegrador.comunicavet.repository;

import com.projetointegrador.comunicavet.model.Clinic;
import com.projetointegrador.comunicavet.model.StaredClinic;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StaredClinicRepository extends CrudRepository<StaredClinic, Long> {
    List<StaredClinic> getByClinic(Clinic clinic);
}

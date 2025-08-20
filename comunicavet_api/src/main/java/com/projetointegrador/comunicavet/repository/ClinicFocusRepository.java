package com.projetointegrador.comunicavet.repository;

import com.projetointegrador.comunicavet.model.Clinic;
import com.projetointegrador.comunicavet.model.ClinicFocus;
import com.projetointegrador.comunicavet.model.Focus;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ClinicFocusRepository extends CrudRepository<ClinicFocus, Long> {
    List<ClinicFocus> findByClinic(Clinic clinic);

    List<ClinicFocus> findByFocus(Focus focus);
}

package com.projetointegrador.comunicavet.repository;

import com.projetointegrador.comunicavet.model.Clinic;
import com.projetointegrador.comunicavet.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClinicRepository extends CrudRepository<Clinic, Long> {
    List<User> findByName(String name);
    Optional<User> findByEmail(String email);
}

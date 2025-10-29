package com.projetointegrador.comunicavet.repository;

import com.projetointegrador.comunicavet.model.ValidCode;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ValidCodeRepository
        extends CrudRepository<ValidCode, Integer> {

    Optional<ValidCode> findByCode(int code);
}

package com.projetointegrador.comunicavet.repository;

import com.projetointegrador.comunicavet.model.State;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StateRepository extends CrudRepository<State, Long> {
    List<State> findByName(String name);

    List<State> findByAbbreviation(String abbreviation);
}

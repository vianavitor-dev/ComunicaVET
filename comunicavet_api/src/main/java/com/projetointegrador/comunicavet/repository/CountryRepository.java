package com.projetointegrador.comunicavet.repository;

import com.projetointegrador.comunicavet.model.Country;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CountryRepository extends CrudRepository<Country, Long> {
    Optional<Country> findByName(String name);

    List<Country> findByAbbreviation(String abbreviation);
}

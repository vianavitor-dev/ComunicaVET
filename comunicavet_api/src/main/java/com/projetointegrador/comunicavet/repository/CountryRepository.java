package com.projetointegrador.comunicavet.repository;

import com.projetointegrador.comunicavet.model.Country;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountryRepository extends CrudRepository<Country, Long> {
    List<Country> findByName(String name);

    List<Country> findByAbbreviation(String abbreviation);
}

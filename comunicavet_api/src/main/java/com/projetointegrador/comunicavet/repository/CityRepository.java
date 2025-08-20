package com.projetointegrador.comunicavet.repository;

import com.projetointegrador.comunicavet.model.City;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends CrudRepository<City, Long> {
    List<City> findByName(String name);
}

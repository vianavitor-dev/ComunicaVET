package com.projetointegrador.comunicavet.repository;

import com.projetointegrador.comunicavet.model.Location;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends CrudRepository<Location, Long> {

    
}

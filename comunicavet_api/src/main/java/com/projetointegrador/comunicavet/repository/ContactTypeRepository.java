package com.projetointegrador.comunicavet.repository;

import com.projetointegrador.comunicavet.model.ContactType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContactTypeRepository extends CrudRepository<ContactType, Byte> {
    Optional<ContactType> findByName(String name);
}

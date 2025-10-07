package com.projetointegrador.comunicavet.repository;

import com.projetointegrador.comunicavet.model.Focus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FocusRepository extends CrudRepository<Focus, Byte> {
    Optional<Focus> findByName(String name);
}

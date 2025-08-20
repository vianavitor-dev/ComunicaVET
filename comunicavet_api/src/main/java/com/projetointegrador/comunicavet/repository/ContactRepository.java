package com.projetointegrador.comunicavet.repository;

import com.projetointegrador.comunicavet.model.Contact;
import com.projetointegrador.comunicavet.model.ContactType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends CrudRepository<Contact, Long> {
    List<Contact> findByValue(String value);

    List<Contact> findByType(ContactType type);
}

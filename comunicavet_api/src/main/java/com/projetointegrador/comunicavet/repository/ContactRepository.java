package com.projetointegrador.comunicavet.repository;

import com.projetointegrador.comunicavet.model.Contact;
import com.projetointegrador.comunicavet.model.ContactType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactRepository extends CrudRepository<Contact, Byte> {
    Optional<Contact> findByValue(String value);

    List<Contact> findByType(ContactType type);

    List<Contact> findByClinicId(Long clinicId);
}

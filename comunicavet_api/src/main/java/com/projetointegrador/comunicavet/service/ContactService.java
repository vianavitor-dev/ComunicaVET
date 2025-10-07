package com.projetointegrador.comunicavet.service;

import com.projetointegrador.comunicavet.dto.contact.ContactDTO;
import com.projetointegrador.comunicavet.dto.contact.RequestContactDTO;
import com.projetointegrador.comunicavet.exceptions.DuplicateResourceException;
import com.projetointegrador.comunicavet.exceptions.NotFoundResourceException;
import com.projetointegrador.comunicavet.mapper.ContactDTOMapper;
import com.projetointegrador.comunicavet.model.Clinic;
import com.projetointegrador.comunicavet.model.Contact;
import com.projetointegrador.comunicavet.model.ContactType;
import com.projetointegrador.comunicavet.repository.ClinicRepository;
import com.projetointegrador.comunicavet.repository.ContactRepository;
import com.projetointegrador.comunicavet.repository.ContactTypeRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactService {
    @Autowired
    private ContactRepository repository;

    @Autowired
    private ContactTypeRepository contactTypeRepository;

    @Autowired
    private ClinicRepository clinicRepository;

    /**
     * Salva um novo Contato no Banco de Dados e o atrela a uma Clínica.
     * Não registra contatos duplicados (na mesma Clínica)
     * @param dto Dados do novo Contato
     * @throws NotFoundResourceException Caso uma das pesquisas ao BD não retorne resultado
     * @throws DuplicateResourceException Caso a Clínica já tenha registrado este contanto anteriormente
     */
    public void register(RequestContactDTO dto)
            throws NotFoundResourceException, DuplicateResourceException {

        // Busca pelo tipo de Contato e Clínica que está vinculando este Contato
        ContactType type = contactTypeRepository.findById(dto.contactTypeId())
                .orElseThrow(() -> new NotFoundResourceException("Tipo de contato não foi encontrado"));

        // Verifica se esta Clínica já registrou este Contato antes
        boolean isDuplicate = repository.findByClinicId(dto.clinicId())
                .stream()
                .anyMatch(c ->
                        // Utiliza o nome do Tipo de Contato
                        // e o campo 'valor' do Contato para verificar
                        // se há duplicatas
                        c.getType().getName().equals(type.getName())
                                && c.getValue().equals(dto.value())
                );

        if (isDuplicate) {
            throw new DuplicateResourceException("Você já registrou esse contato antes");
        }

        Clinic clinic = clinicRepository.findById(dto.clinicId())
                .orElseThrow(() -> new NotFoundResourceException("Clínica não encontrada"));

        Contact contact = ContactDTOMapper.toContact(dto, type, clinic);

        repository.save(contact);
    }

    public Iterable<ContactDTO> getAll() {
        return ((List<Contact>) repository.findAll())
                .stream()
                .map(ContactDTOMapper::toContactDto)
                .toList();
    }

    public Iterable<ContactDTO> getByClinicId(@NotNull Long clinicId) {
        return repository.findByClinicId(clinicId)
                .stream()
                .map(ContactDTOMapper::toContactDto)
                .toList();
    }

    public Iterable<ContactDTO> getByType(@NotNull String contactType) throws NotFoundResourceException {
        ContactType type = contactTypeRepository.findByName(contactType)
                        .orElseThrow(() -> new NotFoundResourceException("Tipo de contato não foi encontrado"));

        return repository.findByType(type)
                .stream()
                .map(ContactDTOMapper::toContactDto)
                .toList();

    }

    public ContactDTO getByValue(@NotNull String value) throws NotFoundResourceException {
        Contact contact = repository.findByValue(value)
                .orElseThrow(() -> new NotFoundResourceException("Contato não encontrado"));

        return ContactDTOMapper.toContactDto(contact);
    }

    public ContactDTO getById(@NotNull Long id) throws NotFoundResourceException {
        Contact contact = repository.findById(id)
                .orElseThrow(() -> new NotFoundResourceException("Contato não encontrado"));

        return ContactDTOMapper.toContactDto(contact);
    }

    public void changeTypeAndValue(RequestContactDTO dto) throws NotFoundResourceException {
        Contact contact = repository.findById(dto.id())
                .orElseThrow(() -> new NotFoundResourceException("Contato não encontrado"));

        ContactType type = contactTypeRepository.findById(dto.contactTypeId())
                .orElseThrow(() -> new NotFoundResourceException("Tipo de contato não foi encontrado"));

        contact.setType(type);
        contact.setValue(dto.value());

        repository.save(contact);
    }

    public void deleteById(@NotNull Long id) throws NotFoundResourceException {
        Contact contact = repository.findById(id)
                .orElseThrow(() -> new NotFoundResourceException("Contato não encontrado"));

        repository.delete(contact);
    }

}

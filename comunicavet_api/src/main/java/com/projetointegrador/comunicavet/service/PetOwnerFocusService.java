package com.projetointegrador.comunicavet.service;

import com.projetointegrador.comunicavet.dto.petOwnerFocus.NewPetOwnerFocusDTO;
import com.projetointegrador.comunicavet.dto.petOwnerFocus.PetOwnerFocusDTO;
import com.projetointegrador.comunicavet.dto.petOwnerFocus.UpdatePetOwnerFocusDTO;
import com.projetointegrador.comunicavet.exceptions.DuplicateResourceException;
import com.projetointegrador.comunicavet.exceptions.NotFoundResourceException;
import com.projetointegrador.comunicavet.mapper.PetOwnerFocusDTOMapper;
import com.projetointegrador.comunicavet.model.Focus;
import com.projetointegrador.comunicavet.model.PetOwner;
import com.projetointegrador.comunicavet.model.PetOwnerFocus;
import com.projetointegrador.comunicavet.repository.FocusRepository;
import com.projetointegrador.comunicavet.repository.PetOwnerFocusRepository;
import com.projetointegrador.comunicavet.repository.PetOwnerRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PetOwnerFocusService {
    @Autowired
    private PetOwnerFocusRepository repository;

    @Autowired
    private PetOwnerRepository petOwnerRepository;

    @Autowired
    private FocusRepository focusRepository;

    /**
     * Escolhe uma série de Focos a serem atrelados ao tipo de serviço que o usuário esta buscando.
     * Não aceita Focos duplicados.
     * @param dto Dados necessários para criação do Foco do Dono de Pet
     * @throws NotFoundResourceException Caso uma das pesquisas ao BD não retorne resultado
     * @throws DuplicateResourceException Caso exista 2 Focos iguais tentando ser registrados
     */
    public void chooseFocus(NewPetOwnerFocusDTO dto)
            throws NotFoundResourceException {

        PetOwner petOwner = petOwnerRepository.findById(dto.petOwnerId())
                .orElseThrow(() -> new NotFoundResourceException("Dono de Pet não encontrado"));

        // Armazena os nomes dos Focos escolhidos pelo usuário
        // para impedir o registro de Focos duplicados no mesmo usuário
        Set<Byte> chosenFocusIds = new HashSet<>();

        for (Byte focusId : dto.focusIds()) {
            if (chosenFocusIds.contains(focusId)) {
                throw new DuplicateResourceException("Você escolheu 2 Focos iguais!");
            }

            chosenFocusIds.add(focusId);
        }

        // Associa o Dono de Pet a vários Focos
        for (Byte focusId : dto.focusIds()) {
            Focus focus = focusRepository.findById(focusId)
                    .orElseThrow(() -> new NotFoundResourceException("Este Foco não existe"));

            PetOwnerFocus petOwnerFocus = PetOwnerFocusDTOMapper.toPetOwnerFocus(focus, petOwner);

            repository.save(petOwnerFocus);
        }
    }

    public Iterable<PetOwnerFocusDTO> getAll() {
        return ((List<PetOwnerFocus>) repository.findAll())
                .stream()
                .map(PetOwnerFocusDTOMapper::toPetOwnerFocusDto)
                .toList();
    }

    public Iterable<PetOwnerFocusDTO> getByPetOwnerId(@NotNull Long petOwnerId)
        throws NotFoundResourceException {

        PetOwner petOwner = petOwnerRepository.findById(petOwnerId)
                .orElseThrow(() -> new NotFoundResourceException("Dono de Pet não encontrado"));

        return repository.findByPetOwner(petOwner)
                .stream()
                .map(PetOwnerFocusDTOMapper::toPetOwnerFocusDto)
                .toList();
    }

    public Iterable<PetOwnerFocusDTO> getByFocusId(@NotNull Byte focusId)
        throws NotFoundResourceException {

        Focus focus = focusRepository.findById(focusId)
                .orElseThrow(() -> new NotFoundResourceException("Este Foco não existe"));

        return repository.findByFocus(focus)
                .stream()
                .map(PetOwnerFocusDTOMapper::toPetOwnerFocusDto)
                .toList();
    }

    public void changeChosenFocus(UpdatePetOwnerFocusDTO dto)
        throws  NotFoundResourceException{

        // Busca o novo Foco que o usuário está interessado
        Focus focus = focusRepository.findById(dto.focusId())
                .orElseThrow(() -> new NotFoundResourceException("Este Foco não existe"));

        PetOwnerFocus petOwnerFocus = repository.findById(dto.id())
                .orElseThrow(() -> new NotFoundResourceException("Foco do Dono de Pet não encontrado"));

        // Muda o Foco que o usuário está buscando
        petOwnerFocus.setFocus(focus);

        repository.save(petOwnerFocus);
    }

    public void deleteById(@NotNull Long id)
        throws NotFoundResourceException {

        PetOwnerFocus petOwnerFocus = repository.findById(id)
                .orElseThrow(() -> new NotFoundResourceException("Foco do Dono de Pet não encontrado"));

        repository.delete(petOwnerFocus);
    }
}






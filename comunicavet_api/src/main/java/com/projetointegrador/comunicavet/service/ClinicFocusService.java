package com.projetointegrador.comunicavet.service;

import com.projetointegrador.comunicavet.dto.clinicFocus.ClinicFocusDTO;
import com.projetointegrador.comunicavet.dto.clinicFocus.NewClinicFocusDTO;
import com.projetointegrador.comunicavet.dto.clinicFocus.UpdateClinicFocusDTO;
import com.projetointegrador.comunicavet.exceptions.DuplicateResourceException;
import com.projetointegrador.comunicavet.exceptions.NotFoundResourceException;
import com.projetointegrador.comunicavet.mapper.ClinicFocusDTOMapper;
import com.projetointegrador.comunicavet.model.*;
import com.projetointegrador.comunicavet.repository.ClinicFocusRepository;
import com.projetointegrador.comunicavet.repository.ClinicRepository;
import com.projetointegrador.comunicavet.repository.FocusRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ClinicFocusService {
    @Autowired
    private ClinicFocusRepository repository;

    @Autowired
    private ClinicRepository clinicRepository;

    @Autowired
    private FocusRepository focusRepository;

    /**
     * Escolhe uma série de Focos a serem atrelados ao tipo de serviço da Clínica.
     * Não aceita Focos duplicados.
     * @param dto Dados necessários para criação do Foco da Clínica
     * @throws NotFoundResourceException Caso uma das pesquisas ao BD não retorne resultado
     * @throws DuplicateResourceException Caso exista 2 Focos iguais tentando ser registrados
     */
    public void chooseFocus(NewClinicFocusDTO dto)
            throws NotFoundResourceException, DuplicateResourceException {

        // Armazena os nomes dos Focos escolhidos pelo usuário
        // para impedir o registro de Focos duplicados no mesmo usuário
        Set<String> chosenFocusName = new HashSet<>();

        for (String focusName : dto.focusNames()) {
            if (chosenFocusName.contains(focusName)) {
                throw new DuplicateResourceException("Você escolheu 2 ou mais Focos iguais!");
            }

            chosenFocusName.add(focusName);
        }

        Clinic clinic = clinicRepository.findById(dto.clinicId())
                .orElseThrow(() -> new NotFoundResourceException("Clínica não encontrado"));

        // Percorre o conjunto de nomes dos Focos escolhidos até o fim
        for (String focusName : dto.focusNames()) {
            Focus focus = focusRepository.findByName(focusName)
                    .orElseThrow(() -> new NotFoundResourceException("Este Foco não existe"));

            /*
                Verifica no Banco de Dados se está Clínica já registrou este Foco
                como um dos tipos de atendimento.
             */
            boolean isDuplicate = repository.findByFocus(focus)
                    .stream()
                    .anyMatch(cf ->
                        cf.getClinic().getEmail().equals(clinic.getEmail())
                    );

            // Apenas prossegue para proxima iteração caso o Foco já tenha sido registrado
            if (isDuplicate) {
                continue;
                // throw new DuplicateResourceException("Você já registrou este Foco antes!");
            }

            ClinicFocus clinicFocus = ClinicFocusDTOMapper.toClinicFocus(clinic, focus);
            repository.save(clinicFocus);
        }
    }

    public Iterable<ClinicFocusDTO> getAll() {
        return ((List<ClinicFocus>) repository.findAll())
                .stream()
                .map(ClinicFocusDTOMapper::toClinicFocusDto)
                .toList();
    }

    public Iterable<ClinicFocusDTO> getByClinicId(@NotNull Long clinicId)
            throws NotFoundResourceException {

        Clinic clinic = clinicRepository.findById(clinicId)
                .orElseThrow(() -> new NotFoundResourceException("Clínica não encontrada"));

        return repository.findByClinic(clinic)
                .stream()
                .map(ClinicFocusDTOMapper::toClinicFocusDto)
                .toList();
    }

    public Iterable<ClinicFocusDTO> getByFocusId(@NotNull Byte focusId)
            throws NotFoundResourceException {

        Focus focus = focusRepository.findById(focusId)
                .orElseThrow(() -> new NotFoundResourceException("Este Foco não existe"));

        return repository.findByFocus(focus)
                .stream()
                .map(ClinicFocusDTOMapper::toClinicFocusDto)
                .toList();
    }

    public void changeChosenFocus(UpdateClinicFocusDTO dto)
            throws  NotFoundResourceException{

        // Busca o novo Foco que o usuário está interessado
        Focus focus = focusRepository.findById(dto.focusId())
                .orElseThrow(() -> new NotFoundResourceException("Este Foco não existe"));

        ClinicFocus clinicFocus = repository.findById(dto.id())
                .orElseThrow(() -> new NotFoundResourceException("Foco da Clínica não encontrado"));

        // Muda o Foco que o usuário está buscando
        clinicFocus.setFocus(focus);

        repository.save(clinicFocus);
    }

    public void deleteById(@NotNull Long id)
            throws NotFoundResourceException {

        ClinicFocus clinicFocus = repository.findById(id)
                .orElseThrow(() -> new NotFoundResourceException("Foco da Clínica não encontrado"));

        repository.delete(clinicFocus);
    }
}






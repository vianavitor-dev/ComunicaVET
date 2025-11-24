package com.projetointegrador.comunicavet.service;

import com.projetointegrador.comunicavet.dto.petClinicHistory.PetClinicRegisterDTO;
import com.projetointegrador.comunicavet.exceptions.NotFoundResourceException;
import com.projetointegrador.comunicavet.mapper.ClinicDTOMapper;
import com.projetointegrador.comunicavet.mapper.PetClinicHistoryDTOMapper;
import com.projetointegrador.comunicavet.model.PetClinicHistory;
import com.projetointegrador.comunicavet.model.Pet;
import com.projetointegrador.comunicavet.model.Clinic;
import com.projetointegrador.comunicavet.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PetClinicHistoryService {

    @Autowired
    private PetClinicHistoryRepository petClinicHistoryRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private ClinicRepository clinicRepository;

    @Autowired
    private FavoriteClinicRepository favoriteClinicRepository;

    public void register(Long clinicId, Long petId) throws NotFoundResourceException {
        Clinic clinic = clinicRepository.findById(clinicId)
                .orElseThrow(() -> new NotFoundResourceException("Clinic not found"));

        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new NotFoundResourceException("Pet not found"));

        PetClinicHistory history = new PetClinicHistory();
        history.setClinic(clinic);
        history.setPet(pet);
        history.setRegisteredAt(LocalDate.now());

        petClinicHistoryRepository.save(history);
    }

    public void deleteById(Long id) {
        petClinicHistoryRepository.deleteById(id);
    }

    public PetClinicRegisterDTO getById(Long id) throws NotFoundResourceException {
        PetClinicHistory history = petClinicHistoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundResourceException("Registro de agendamento do Pet não encontrado"));

        return PetClinicHistoryDTOMapper.toPetClinicRegisterDTO(history);
    }

    public List<PetClinicRegisterDTO> getByPet(Long petId) throws NotFoundResourceException{
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new NotFoundResourceException("Pet not found"));

        List<PetClinicHistory> histories = petClinicHistoryRepository.findByPet(pet);
        return histories.stream()
                .map(PetClinicHistoryDTOMapper::toPetClinicRegisterDTO)
                .collect(Collectors.toList());
    }
}
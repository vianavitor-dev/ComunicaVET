package com.projetointegrador.comunicavet.service;

import com.projetointegrador.comunicavet.dto.favoriteClinic.FavoriteClinicDTO;
import com.projetointegrador.comunicavet.dto.favoriteClinic.RequestFavoriteClinicDTO;
import com.projetointegrador.comunicavet.exceptions.NotFoundResourceException;
import com.projetointegrador.comunicavet.mapper.FavoriteClinicDTOMapper;
import com.projetointegrador.comunicavet.model.Clinic;
import com.projetointegrador.comunicavet.model.FavoriteClinic;
import com.projetointegrador.comunicavet.model.PetOwner;
import com.projetointegrador.comunicavet.repository.ClinicRepository;
import com.projetointegrador.comunicavet.repository.FavoriteClinicRepository;
import com.projetointegrador.comunicavet.repository.PetOwnerRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteClinicService {
    @Autowired
    private FavoriteClinicRepository repository;

    @Autowired
    private PetOwnerRepository petOwnerRepository;

    @Autowired
    private ClinicRepository clinicRepository;

    // REMOVIDO: ContactRepository

    public void add(RequestFavoriteClinicDTO dto) throws NotFoundResourceException {
        PetOwner petOwner = petOwnerRepository.findById(dto.petOwnerId())
                .orElseThrow(() -> new NotFoundResourceException("Dono de Pet não encontrado"));

        Clinic clinic = clinicRepository.findById(dto.clinicId())
                .orElseThrow(() -> new NotFoundResourceException("Clínica não encontrada"));

        FavoriteClinic favoriteClinic = FavoriteClinicDTOMapper
                .toFavoriteClinic(clinic, petOwner);

        repository.save(favoriteClinic);
    }

    public Iterable<FavoriteClinicDTO> getAll() {
        // REMOVIDO: Busca de contatos
        return ((List<FavoriteClinic>) repository.findAll())
                .stream()
                .map(FavoriteClinicDTOMapper::toFavoriteClinicDTO)
                .toList();
    }

    public Iterable<FavoriteClinicDTO> getByClinicId(@NotNull Long clinicId)
            throws NotFoundResourceException {

        Clinic clinic = clinicRepository.findById(clinicId)
                .orElseThrow(() -> new NotFoundResourceException("Clínica não encontrada"));

        // REMOVIDO: Busca de contatos
        return repository.findByClinic(clinic)
                .stream()
                .map(FavoriteClinicDTOMapper::toFavoriteClinicDTO)
                .toList();
    }

    public Iterable<FavoriteClinicDTO> getByPetOwnerId(@NotNull Long petOwnerId)
            throws NotFoundResourceException {

        PetOwner petOwner = petOwnerRepository.findById(petOwnerId)
                .orElseThrow(() -> new NotFoundResourceException("Dono de Pet não encontrado"));

        // REMOVIDO: Busca de contatos
        return repository.findByPetOwner(petOwner)
                .stream()
                .map(FavoriteClinicDTOMapper::toFavoriteClinicDTO)
                .toList();
    }

    public FavoriteClinicDTO getById(@NotNull Long id) throws  NotFoundResourceException {
        FavoriteClinic favoriteClinic = repository.findById(id)
                .orElseThrow(() -> new NotFoundResourceException("Clinica Favoritada não encontrada"));

        // REMOVIDO: Busca de contatos
        return FavoriteClinicDTOMapper.toFavoriteClinicDTO(favoriteClinic);
    }

    public void removeById(@NotNull Long clinicId) throws NotFoundResourceException {
        Clinic clinic = clinicRepository.findById(clinicId)
                .orElseThrow(() -> new NotFoundResourceException("Clínica não encontrada"));

        var favoritedClinics = repository.findByClinic(clinic);

        repository.deleteAll(favoritedClinics);
    }
}

package com.projetointegrador.comunicavet.service;

import com.projetointegrador.comunicavet.dto.pet.PetCardDTO;
import com.projetointegrador.comunicavet.dto.pet.PetDTO;
import com.projetointegrador.comunicavet.exceptions.NotFoundResourceException;
import com.projetointegrador.comunicavet.model.Pet;
import com.projetointegrador.comunicavet.model.PetOwner;
import com.projetointegrador.comunicavet.repository.PetOwnerRepository;
import com.projetointegrador.comunicavet.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PetService {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private PetOwnerRepository petOwnerRepository;

    public void addPet(PetDTO dto) throws NotFoundResourceException {
        PetOwner owner = petOwnerRepository.findById(dto.ownerId())
                .orElseThrow(() -> new NotFoundResourceException("Owner not found"));

        Pet pet = new Pet();
        pet.setName(dto.name());
        pet.setType(dto.type());
        pet.setBreed(dto.breed());
        pet.setSecondBreed(dto.secondBreed());
        pet.setSize(dto.size());
        pet.setDateBirth(dto.dateBirth());
        pet.setPetOwner(owner);

        petRepository.save(pet);
    }

    public void removePet(Long id) {
        petRepository.deleteById(id);
    }

    public List<PetCardDTO> getByOwner(Long ownerId) throws NotFoundResourceException {
        PetOwner owner = petOwnerRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundResourceException("Owner not found"));

        List<Pet> pets = petRepository.findByPetOwner(owner);

        return pets.stream()
                .map(pet -> new PetCardDTO(pet.getId(), pet.getName(), pet.getBreed()))
                .collect(Collectors.toList());
    }

    public PetDTO getById(Long id) throws NotFoundResourceException {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new NotFoundResourceException("Pet not found"));

        return new PetDTO(
                pet.getId(),
                pet.getName(),
                pet.getType(),
                pet.getBreed(),
                pet.getSecondBreed(),
                pet.getSize(),
                pet.getDateBirth(),
                pet.getPetOwner().getId()
        );
    }
}
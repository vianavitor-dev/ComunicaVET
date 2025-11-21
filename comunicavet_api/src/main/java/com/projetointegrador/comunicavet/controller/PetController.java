package com.projetointegrador.comunicavet.controller;

import com.projetointegrador.comunicavet.dto.pet.PetCardDTO;
import com.projetointegrador.comunicavet.dto.pet.PetDTO;
import com.projetointegrador.comunicavet.service.PetService;
import com.projetointegrador.comunicavet.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pets")
public class PetController {

    @Autowired
    private PetService petService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> addPet(@RequestBody PetDTO dto) {
        petService.addPet(dto);

        return new ResponseEntity<>(new ApiResponse<>(false, "Pet registrado", null), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removePet(@PathVariable Long id) {
        petService.removePet(id);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<ApiResponse<List<PetCardDTO>>> getByOwner(@PathVariable Long ownerId) {
        var pets = petService.getByOwner(ownerId);

        return ResponseEntity.ok(new ApiResponse<>(false, "Lista de Pets que pertencem ao Dono de Pet", pets));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PetDTO>> getById(@PathVariable Long id) {
        var pet = petService.getById(id);

        return ResponseEntity.ok(new ApiResponse<>(false, "Pet pelo Id", pet));
    }
}


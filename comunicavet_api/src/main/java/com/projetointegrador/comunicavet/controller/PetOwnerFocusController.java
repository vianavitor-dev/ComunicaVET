package com.projetointegrador.comunicavet.controller;

import com.projetointegrador.comunicavet.dto.petOwnerFocus.NewPetOwnerFocusDTO;
import com.projetointegrador.comunicavet.dto.petOwnerFocus.PetOwnerFocusDTO;
import com.projetointegrador.comunicavet.dto.petOwnerFocus.UpdatePetOwnerFocusDTO;
import com.projetointegrador.comunicavet.service.PetOwnerFocusService;
import com.projetointegrador.comunicavet.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pet-owner-focuses")
@CrossOrigin("*")
public class PetOwnerFocusController {

    @Autowired
    private PetOwnerFocusService service;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> chooseFocus(@RequestBody NewPetOwnerFocusDTO dto) {
        service.chooseFocus(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(false, "Foco atribuído ao dono de pet", null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Iterable<PetOwnerFocusDTO>>> getAll() {
        var list = service.getAll();
        return ResponseEntity.ok(new ApiResponse<>(false, "Lista de focos dos donos de pet", list));
    }

    @GetMapping("/owner/{petOwnerId}")
    public ResponseEntity<ApiResponse<Iterable<PetOwnerFocusDTO>>> getByPetOwnerId(@PathVariable Long petOwnerId) {
        var list = service.getByPetOwnerId(petOwnerId);
        return ResponseEntity.ok(new ApiResponse<>(false, "Focos do dono de pet", list));
    }

    @GetMapping("/focus/{focusId}")
    public ResponseEntity<ApiResponse<Iterable<PetOwnerFocusDTO>>> getByFocusId(@PathVariable Byte focusId) {
        var list = service.getByFocusId(focusId);
        return ResponseEntity.ok(new ApiResponse<>(false, "Donos que têm esse foco", list));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<?>> changeChosenFocus(@RequestBody UpdatePetOwnerFocusDTO dto) {
        service.changeChosenFocus(dto);
        return ResponseEntity.ok(new ApiResponse<>(false, "Foco do dono de pet alterado", null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteById(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.ok(new ApiResponse<>(false, "Foco removido", null));
    }
}
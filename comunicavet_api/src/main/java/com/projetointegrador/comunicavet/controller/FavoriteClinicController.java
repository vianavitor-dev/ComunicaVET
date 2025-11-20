package com.projetointegrador.comunicavet.controller;

import com.projetointegrador.comunicavet.dto.favoriteClinic.FavoriteClinicDTO;
import com.projetointegrador.comunicavet.dto.favoriteClinic.RequestFavoriteClinicDTO;
import com.projetointegrador.comunicavet.service.FavoriteClinicService;
import com.projetointegrador.comunicavet.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/favorite-clinics")
public class FavoriteClinicController {

    @Autowired
    private FavoriteClinicService service;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> add(@RequestBody RequestFavoriteClinicDTO dto) {
        service.add(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(false, "Clínica favoritada", null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Iterable<FavoriteClinicDTO>>> getAll() {
        var list = service.getAll();
        return ResponseEntity.ok(new ApiResponse<>(false, "Lista de favoritos", list));
    }

    @GetMapping("/clinic/{clinicId}")
    public ResponseEntity<ApiResponse<Iterable<FavoriteClinicDTO>>> getByClinicId(@PathVariable Long clinicId) {
        var list = service.getByClinicId(clinicId);
        return ResponseEntity.ok(new ApiResponse<>(false, "Favoritos por clínica", list));
    }

    @GetMapping("/owner/{petOwnerId}")
    public ResponseEntity<ApiResponse<Iterable<FavoriteClinicDTO>>> getByPetOwnerId(@PathVariable Long petOwnerId) {
        var list = service.getByPetOwnerId(petOwnerId);
        return ResponseEntity.ok(new ApiResponse<>(false, "Favoritos do dono de pet", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FavoriteClinicDTO>> getById(@PathVariable Long id) {
        var dto = service.getById(id);
        return ResponseEntity.ok(new ApiResponse<>(false, "Favorito encontrado", dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> removeById(@PathVariable Long id) {
        service.removeById(id);
        return ResponseEntity.ok(new ApiResponse<>(false, "Favorito removido", null));
    }
}

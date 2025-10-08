package com.projetointegrador.comunicavet.controller;

import com.projetointegrador.comunicavet.dto.petOwner.NewPetOwnerDTO;
import com.projetointegrador.comunicavet.dto.petOwner.PetOwnerDTO;
import com.projetointegrador.comunicavet.dto.user.LoginDTO;
import com.projetointegrador.comunicavet.utils.ApiResponse;
import com.projetointegrador.comunicavet.service.PetOwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pet-owners")
@CrossOrigin("*")
public class PetOwnerController {

    @Autowired
    private PetOwnerService service;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> register(@RequestBody NewPetOwnerDTO dto) {
        service.register(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(false, "Dono de pet registrado", null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Iterable<PetOwnerDTO>>> getAll() {
        var list = service.getAll();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(false, "Lista de donos de pet", list));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Iterable<PetOwnerDTO>>> getByName(@RequestParam("name") String name) {
        var list = service.getByName(name);

        return ResponseEntity.ok(new ApiResponse<>(false, "Donos encontrados", list));
    }

    @GetMapping("/by-email")
    public ResponseEntity<ApiResponse<PetOwnerDTO>> getByEmail(@RequestParam("email") String email) {
        var dto = service.getByEmail(email);

        return ResponseEntity.ok(new ApiResponse<>(false, "Dono encontrado", dto));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Long>> logIn(@RequestBody LoginDTO login) {
        Long id = service.logIn(login);

        return ResponseEntity.ok(new ApiResponse<>(false, "Login concluído", id));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<?>> changeNameAndEmail(@RequestBody PetOwnerDTO dto) {
        service.changeNameAndEmail(dto);

        return ResponseEntity.ok(new ApiResponse<>(false, "Nome e e-mail alterados", null));
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<ApiResponse<?>> changePassword(@PathVariable Long id, @RequestParam("newPassword") String newPassword) {
        service.changePassword(id, newPassword);

        return ResponseEntity.ok(new ApiResponse<>(false, "Senha alterada", null));
    }

    @PatchMapping("/{id}/profile-image")
    public ResponseEntity<ApiResponse<?>> changeProfileImage(@PathVariable Long id, @RequestParam("path") String newImagePath) {
        service.changeProfileImage(id, newImagePath);

        return ResponseEntity.ok(new ApiResponse<>(false, "Imagem de perfil alterada", null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteById(@PathVariable Long id) {
        service.deleteById(id);

        return ResponseEntity.ok(new ApiResponse<>(false, "Dono de pet removido", null));
    }
}

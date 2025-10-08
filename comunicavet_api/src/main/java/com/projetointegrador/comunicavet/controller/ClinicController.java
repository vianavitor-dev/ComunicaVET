package com.projetointegrador.comunicavet.controller;

import com.projetointegrador.comunicavet.dto.clinic.ClinicDTO;
import com.projetointegrador.comunicavet.dto.clinic.NewClinicDTO;
import com.projetointegrador.comunicavet.dto.user.LoginDTO;
import com.projetointegrador.comunicavet.service.ClinicService;
import com.projetointegrador.comunicavet.utils.ApiResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/clinics")
@CrossOrigin("*")
public class ClinicController {

    @Autowired
    private ClinicService service;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> register(@RequestBody NewClinicDTO dto) {
        service.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(false, "Clínica registrada", null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Iterable<ClinicDTO>>> getAll() {
        var list = service.getAll();
        return ResponseEntity.ok(new ApiResponse<>(false, "Lista de clínicas", list));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Iterable<ClinicDTO>>> getByName(@RequestParam("name") String name) {
        var list = service.getByName(name);
        return ResponseEntity.ok(new ApiResponse<>(false, "Clínicas encontradas", list));
    }

    @GetMapping("/by-email")
    public ResponseEntity<ApiResponse<ClinicDTO>> getByEmail(@RequestParam("email") String email) {
        var dto = service.getByEmail(email);
        return ResponseEntity.ok(new ApiResponse<>(false, "Clínica encontrada", dto));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Long>> login(@RequestBody LoginDTO dto) {
        Long id = service.logIn(dto);
        return ResponseEntity.ok(new ApiResponse<>(false, "Login concluído", id));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<?>> changeNameAndEmail(@RequestBody ClinicDTO dto) {
        service.changeNameAndEmail(dto);
        return ResponseEntity.ok(new ApiResponse<>(false, "Nome/e-mail alterados", null));
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

    @PatchMapping("/{id}/background-image")
    public ResponseEntity<ApiResponse<?>> changeBackgroundImage(@PathVariable Long id, @RequestParam("path") String newImagePath) {
        service.changeBackgroundImage(id, newImagePath);
        return ResponseEntity.ok(new ApiResponse<>(false, "Imagem de fundo alterada", null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteById(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.ok(new ApiResponse<>(false, "Clínica removida", null));
    }
}

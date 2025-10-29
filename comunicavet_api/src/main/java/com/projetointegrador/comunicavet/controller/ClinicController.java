package com.projetointegrador.comunicavet.controller;

import com.projetointegrador.comunicavet.dto.clinic.*;
import com.projetointegrador.comunicavet.dto.user.LoginDTO;
import com.projetointegrador.comunicavet.exceptions.NotFoundResourceException;
import com.projetointegrador.comunicavet.service.ClinicService;
import com.projetointegrador.comunicavet.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/clinics")
@CrossOrigin("*")
public class ClinicController {

    @Autowired
    private ClinicService service;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> register(@RequestBody NewClinicDTO dto)
        throws IllegalAccessException {

        Long id = service.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(false, "Clínica registrada", id));
    }

//    @GetMapping
//    public ResponseEntity<ApiResponse<Iterable<ClinicDTO>>> getAll() {
//        var list = service.getAll();
//        return ResponseEntity.ok(new ApiResponse<>(false, "Lista de clínicas", list));
//    }

    @PostMapping("/{id}/rate")
    public ResponseEntity<ApiResponse<?>> rateClinic
            (@PathVariable Long id, @RequestParam("userId") Long userId, @RequestParam Integer stars) {

        service.rateClinic(id, userId, stars);
        return ResponseEntity.ok(new ApiResponse<>(false, "Avaliação recebida", null));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Iterable<ClinicCardDTO>>> getByName(@RequestParam("name") String name) {
        var list = service.getByName(name);
        return ResponseEntity.ok(new ApiResponse<>(false, "Clínicas encontradas", list));
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<ApiResponse<ClinicProfileDTO>> profile(@PathVariable Long id) {
        var dto = service.viewProfile(id);
        return ResponseEntity.ok(new ApiResponse<>(false, "Perfil da Clínica", dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ClinicInfoDTO>> view(@PathVariable Long id) {
        var dto = service.showInfo(id);

        return ResponseEntity.ok(new ApiResponse<>(false, "Informações da Clínica", dto));
    }

//    @GetMapping("/by-email")
//    public ResponseEntity<ApiResponse<ClinicDTO>> getByEmail(@RequestParam("email") String email) {
//        var dto = service.getByEmail(email);
//        return ResponseEntity.ok(new ApiResponse<>(false, "Clínica encontrada", dto));
//    }

    @PostMapping("/filter")
    public ResponseEntity<ApiResponse<Iterable<?>>> filter
            (@RequestBody ClinicFilterDTO dto) throws NotFoundResourceException, IllegalAccessException {

        Iterable<ClinicCardDTO> result =  service.filter(
                dto.userId(), Optional.ofNullable(dto.tagNames()), Optional.ofNullable(dto.newAddressDto())
        );

        return ResponseEntity.ok(new ApiResponse<>(false, "Filtro aplicado", result));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Long>> login(@RequestBody LoginDTO dto) {
        Long id = service.logIn(dto);
        return ResponseEntity.ok(new ApiResponse<>(false, "Login concluído", id));
    }

    @PutMapping("/profile/{id}")
    public ResponseEntity<ApiResponse<?>> editProfile
            (@PathVariable Long id, @RequestBody ClinicProfileDTO dto,
             @RequestParam boolean modifyAddress, @RequestParam boolean modifyContacts)
            throws IllegalAccessException {

        service.editProfile(id, dto, modifyAddress, modifyContacts);
        return ResponseEntity.ok(new ApiResponse<>(false, "Perfil da clínica editado", null));
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<ApiResponse<?>> changePassword
            (@PathVariable Long id, @RequestParam("newPassword") String newPassword) {

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

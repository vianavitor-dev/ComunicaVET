package com.projetointegrador.comunicavet.controller;

import com.projetointegrador.comunicavet.dto.image.ImageAndContentTypeDTO;
import com.projetointegrador.comunicavet.dto.petOwner.NewPetOwnerDTO;
import com.projetointegrador.comunicavet.dto.petOwner.PetOwnerDTO;
import com.projetointegrador.comunicavet.dto.petOwner.PetOwnerProfileDTO;
import com.projetointegrador.comunicavet.dto.user.LoginDTO;
import com.projetointegrador.comunicavet.service.ImageService;
import com.projetointegrador.comunicavet.utils.ApiResponse;
import com.projetointegrador.comunicavet.service.PetOwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/pet-owners")
public class PetOwnerController {

    @Autowired
    private PetOwnerService service;

    @Autowired
    private ImageService imageService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> register(@RequestBody NewPetOwnerDTO dto) throws IllegalAccessException {
        Long id = service.register(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(false, "Dono de pet registrado", id));
    }

//    @GetMapping
//    public ResponseEntity<ApiResponse<Iterable<PetOwnerDTO>>> getAll() {
//        var list = service.getAll();
//
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(new ApiResponse<>(false, "Lista de donos de pet", list));
//    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<ApiResponse<PetOwnerProfileDTO>> profile(@PathVariable Long id) {
        PetOwnerProfileDTO dto = service.viewProfile(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(false, "Perfil do Dono de Pet", dto));
    }

//    @GetMapping("/search")
//    public ResponseEntity<ApiResponse<Iterable<PetOwnerDTO>>> getByName(@RequestParam("name") String name) {
//        var list = service.getByName(name);
//
//        return ResponseEntity.ok(new ApiResponse<>(false, "Donos encontrados", list));
//    }

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

    @PutMapping("/profile/{id}")
    public ResponseEntity<ApiResponse<?>> editProfile
            (@PathVariable Long id, @RequestBody PetOwnerProfileDTO dto) throws IllegalAccessException {

        service.editProfile(id, dto);

        return ResponseEntity.ok(new ApiResponse<>(false, "Dados editados com sucesso", null));
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<ApiResponse<?>> changePassword(@PathVariable Long id, @RequestParam("newPassword") String newPassword) {
        service.changePassword(id, newPassword);

        return ResponseEntity.ok(new ApiResponse<>(false, "Senha alterada", null));
    }

    @PostMapping(value = "/{id}/profile-image", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<?>> changeProfileImage
            (@PathVariable Long id, @RequestPart("file") MultipartFile file) throws IOException {

        service.changeProfileImage(id, file);

        return ResponseEntity.ok(new ApiResponse<>(false, "Imagem de perfil alterada", null));
    }

//    @GetMapping("/{id}/profile-image")
//    public ResponseEntity<?> getProfileImage
//            (@PathVariable Long id) throws IOException {
//
//        ImageAndContentTypeDTO result = imageService.getImageAndContentType(id, false);
//
//        return ResponseEntity
//                .ok()
//                .contentType(MediaType.parseMediaType(result.contentType()))
//                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + result.image().getFilename() + "\"")
//                .body(result.image());
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteById(@PathVariable Long id) {
        service.deleteById(id);

        return ResponseEntity.ok(new ApiResponse<>(false, "Dono de pet removido", null));
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ApiResponse<?>> handleIOException(IOException e) {
        return new ResponseEntity<>(
                new ApiResponse<>(true, "Erro ao salvar imagem", null),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}

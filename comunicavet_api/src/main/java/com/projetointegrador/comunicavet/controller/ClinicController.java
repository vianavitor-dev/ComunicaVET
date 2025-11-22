package com.projetointegrador.comunicavet.controller;

import com.projetointegrador.comunicavet.dto.clinic.*;
import com.projetointegrador.comunicavet.dto.image.ImageAndContentTypeDTO;
import com.projetointegrador.comunicavet.dto.user.LoginDTO;
import com.projetointegrador.comunicavet.exceptions.NotFoundResourceException;
import com.projetointegrador.comunicavet.service.ClinicService;
import com.projetointegrador.comunicavet.service.ImageService;
import com.projetointegrador.comunicavet.utils.ApiResponse;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/clinics")
public class ClinicController {

    @Autowired
    private ClinicService service;

    @Autowired
    private ImageService imageService;

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

    @GetMapping("/{id:[0-9]+}")
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
             @RequestParam boolean modifyAddress) throws IllegalAccessException { // REMOVIDO: modifyContacts

        service.editProfile(id, dto, modifyAddress, false); // ALTERADO: modifyContacts sempre false
        return ResponseEntity.ok(new ApiResponse<>(false, "Perfil da clínica editado", null));
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<ApiResponse<?>> changePassword
            (@PathVariable Long id, @RequestParam("newPassword") String newPassword) {

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
//    public ResponseEntity<ApiResponse<?>> getProfileImage
//            (@PathVariable Long id) throws IOException {
//
//        ImageAndContentTypeDTO result = imageService.getImageAndContentType(id, false);
//
//        return ResponseEntity
//                .ok()
//                .contentType(MediaType.parseMediaType(result.contentType()))
//                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + result.image().getFilename() + "\"")
//                .body(new ApiResponse<>(false, "Imagem encontrada", result.image()));
//    }

    @PostMapping(value = "/{id}/background-image", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<?>> changeBackgroundImage
            (@PathVariable Long id, @RequestPart("file") MultipartFile file) throws IOException {

        service.changeBackgroundImage(id, file);
        return ResponseEntity.ok(new ApiResponse<>(false, "Imagem de fundo alterada", null));
    }

    @GetMapping("/{id}/background-image")
    public ResponseEntity<Resource> getBackgroundImage
            (@PathVariable Long id) throws IOException {

        ImageAndContentTypeDTO result = imageService
                .getImageAndContentType(Optional.ofNullable(id), Optional.empty(), true);

        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(result.contentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + result.image().getFilename() + "\"")
                .body(result.image());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteById(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.ok(new ApiResponse<>(false, "Clínica removida", null));
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ApiResponse<?>> handleIOException(IOException e) {
        return new ResponseEntity<>(
                new ApiResponse<>(true, "Erro ao salvar imagem", null),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}

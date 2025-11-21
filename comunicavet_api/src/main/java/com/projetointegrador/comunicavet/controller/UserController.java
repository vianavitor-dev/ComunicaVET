package com.projetointegrador.comunicavet.controller;

import com.projetointegrador.comunicavet.dto.image.ImageAndContentTypeDTO;
import com.projetointegrador.comunicavet.dto.user.LoginDTO;
import com.projetointegrador.comunicavet.dto.user.UserIdentityDTO;
import com.projetointegrador.comunicavet.model.User;
import com.projetointegrador.comunicavet.service.ImageService;
import com.projetointegrador.comunicavet.service.UserService;
import com.projetointegrador.comunicavet.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    private UserService service;

    @Autowired
    private ImageService imageService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserIdentityDTO>> logIn(@RequestBody LoginDTO dto) {
        var data = service.logIn(dto);
        return ResponseEntity.ok(new ApiResponse<>(false, "Login concluído", data));
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<ApiResponse<?>> changePassword(@PathVariable Long id, @RequestParam String newPassword) {
        service.changePassword(id, newPassword);

        return ResponseEntity.ok(new ApiResponse<>(false, "Senha alterada", null));
    }

    @GetMapping("/profile-image")
    public ResponseEntity<Resource> getProfileImage
            (@RequestParam(required = false) String email,
             @RequestParam(required = false) Long id
            ) throws IOException {

        System.out.println("Carregando Imagem...");

        Optional<String> optionalEmail = Optional.ofNullable(email);
        Optional<Long> optionalId = Optional.ofNullable(id);

        ImageAndContentTypeDTO result = imageService.getImageAndContentType(optionalId, optionalEmail, false);

        System.out.println("Imagem carregada!");

        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(result.contentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + result.image().getFilename() + "\"")
                .body(result.image());
    }

    @DeleteMapping("/deactivate/{id}")
    public ResponseEntity<ApiResponse<?>> deactivate(@PathVariable Long id, @RequestBody LoginDTO dto) {
        service.deactivate(id, dto.password());

        return ResponseEntity.ok(new ApiResponse<>(false, "Usuário deletado!", null));
    }
}

package com.projetointegrador.comunicavet.controller;

import com.projetointegrador.comunicavet.dto.user.LoginDTO;
import com.projetointegrador.comunicavet.dto.user.UserIdentityDTO;
import com.projetointegrador.comunicavet.model.User;
import com.projetointegrador.comunicavet.service.UserService;
import com.projetointegrador.comunicavet.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin("*")
public class UserController {
    @Autowired
    private UserService service;

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

    @DeleteMapping("/deactivate/{id}")
    public ResponseEntity<ApiResponse<?>> deactivate(@PathVariable Long id, @RequestBody LoginDTO dto) {
        service.deactivate(id, dto.password());

        return ResponseEntity.ok(new ApiResponse<>(false, "Usuário deletado!", null));
    }
}

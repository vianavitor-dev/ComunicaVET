package com.projetointegrador.comunicavet.controller;

import com.projetointegrador.comunicavet.service.ValidCodeService;
import com.projetointegrador.comunicavet.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/recover-passwords")
public class ValidCodeController {
    @Autowired
    private ValidCodeService service;

    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<?>> verifyCode(@RequestParam Integer code) {
        String result = service.isThisCodeValid(code) ? "Valido" : "Inválido";
        HttpStatus status = HttpStatus.UNAUTHORIZED;

        // Deleta este registro caso ele sejá valido
        if (result.equals("Valido")) {
            service.deleteByCode(code);
            status = HttpStatus.OK;
        }

        return ResponseEntity
                .status(status)
                .body(new ApiResponse<>(false, "Este código é " + result, result.equals("Valido")));
    }

}

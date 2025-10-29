package com.projetointegrador.comunicavet.controller;

import com.projetointegrador.comunicavet.service.EmailService;
import com.projetointegrador.comunicavet.service.ValidCodeService;
import com.projetointegrador.comunicavet.utils.ApiResponse;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/email-senders")
@CrossOrigin("*")
public class EmailController {
    @Autowired
    private EmailService service;

    @Autowired
    private ValidCodeService validCodeService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> sendCode(@RequestParam("to") String userEmail) throws MessagingException {
        Long userId = service.sendValidRecoverCode(userEmail);
        return ResponseEntity.ok(new ApiResponse<>(false, "Email enviado", userId));
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<ApiResponse<?>> handleMessagingException(MessagingException ex) {
        return ResponseEntity
                .internalServerError()
                .body(new ApiResponse<>(true, ex.getMessage(), null));
    }
}

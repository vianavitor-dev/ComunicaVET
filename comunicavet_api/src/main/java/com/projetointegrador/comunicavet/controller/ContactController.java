package com.projetointegrador.comunicavet.controller;

import com.projetointegrador.comunicavet.dto.contact.ContactDTO;
import com.projetointegrador.comunicavet.dto.contact.RequestContactDTO;
import com.projetointegrador.comunicavet.service.ContactService;
import com.projetointegrador.comunicavet.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/contacts")
@CrossOrigin("*")
public class ContactController {

    @Autowired
    private ContactService service;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> register(@RequestBody RequestContactDTO dto) {
        service.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(false, "Contato criado", null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Iterable<ContactDTO>>> getAll() {
        var list = service.getAll();
        return ResponseEntity.ok(new ApiResponse<>(false, "Lista de contatos", list));
    }

    @GetMapping("/clinic/{clinicId}")
    public ResponseEntity<ApiResponse<Iterable<ContactDTO>>> getByClinicId(@PathVariable Long clinicId) {
        var list = service.getByClinicId(clinicId);
        return ResponseEntity.ok(new ApiResponse<>(false, "Contatos da clínica", list));
    }

    @GetMapping("/type/{contactType}")
    public ResponseEntity<ApiResponse<Iterable<ContactDTO>>> getByType(@PathVariable String contactType) {
        var list = service.getByType(contactType);
        return ResponseEntity.ok(new ApiResponse<>(false, "Contatos por tipo", list));
    }

    @GetMapping("/by-value")
    public ResponseEntity<ApiResponse<ContactDTO>> getByValue(@RequestParam("value") String value) {
        var dto = service.getByValue(value);
        return ResponseEntity.ok(new ApiResponse<>(false, "Contato encontrado", dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ContactDTO>> getById(@PathVariable Long id) {
        var dto = service.getById(id);
        return ResponseEntity.ok(new ApiResponse<>(false, "Contato encontrado", dto));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<?>> changeTypeAndValue(@RequestBody RequestContactDTO dto) {
        service.changeTypeAndValue(dto);
        return ResponseEntity.ok(new ApiResponse<>(false, "Contato alterado", null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteById(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.ok(new ApiResponse<>(false, "Contato removido", null));
    }
}

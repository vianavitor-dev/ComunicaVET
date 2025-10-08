package com.projetointegrador.comunicavet.controller;

import com.projetointegrador.comunicavet.dto.clinicFocus.ClinicFocusDTO;
import com.projetointegrador.comunicavet.dto.clinicFocus.NewClinicFocusDTO;
import com.projetointegrador.comunicavet.dto.clinicFocus.UpdateClinicFocusDTO;
import com.projetointegrador.comunicavet.service.ClinicFocusService;
import com.projetointegrador.comunicavet.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/clinic-focuses")
@CrossOrigin("*")
public class ClinicFocusController {

    @Autowired
    private ClinicFocusService service;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> chooseFocus(@RequestBody NewClinicFocusDTO dto) {
        service.chooseFocus(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(false, "Foco adicionado à clínica", null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Iterable<ClinicFocusDTO>>> getAll() {
        var list = service.getAll();
        return ResponseEntity.ok(new ApiResponse<>(false, "Lista de focos de clínica", list));
    }

    // GET: /api/v1/clinic-focuses/clinic/?
    @GetMapping("/clinic/{clinicId}")
    public ResponseEntity<ApiResponse<Iterable<ClinicFocusDTO>>> getByClinicId(@PathVariable Long clinicId) {
        var list = service.getByClinicId(clinicId);
        return ResponseEntity.ok(new ApiResponse<>(false, "Focos da clínica", list));
    }

    @GetMapping("/focus/{focusId}")
    public ResponseEntity<ApiResponse<Iterable<ClinicFocusDTO>>> getByFocusId(@PathVariable Byte focusId) {
        var list = service.getByFocusId(focusId);
        return ResponseEntity.ok(new ApiResponse<>(false, "Clínicas com foco", list));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<?>> changeChosenFocus(@RequestBody UpdateClinicFocusDTO dto) {
        service.changeChosenFocus(dto);
        return ResponseEntity.ok(new ApiResponse<>(false, "Foco alterado", null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteById(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.ok(new ApiResponse<>(false, "Foco da clínica removido", null));
    }
}

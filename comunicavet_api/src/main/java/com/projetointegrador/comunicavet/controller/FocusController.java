package com.projetointegrador.comunicavet.controller;

import com.projetointegrador.comunicavet.dto.focus.FocusDTO;
import com.projetointegrador.comunicavet.service.FocusService;
import com.projetointegrador.comunicavet.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/focuses")
@CrossOrigin("*")
public class FocusController {

    @Autowired
    private FocusService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Iterable<FocusDTO>>> getAll() {
        var list = service.getAll();
        return ResponseEntity.ok(new ApiResponse<>(false, "Lista de focos", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FocusDTO>> getById(@PathVariable Byte id) {
        var dto = service.getById(id);
        return ResponseEntity.ok(new ApiResponse<>(false, "Foco encontrado", dto));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<FocusDTO>> getByName(@RequestParam("name") String name) {
        var dto = service.getByName(name);
        return ResponseEntity.ok(new ApiResponse<>(false, "Foco encontrado por nome", dto));
    }
}

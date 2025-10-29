package com.projetointegrador.comunicavet.controller;

import com.projetointegrador.comunicavet.model.Country;
import com.projetointegrador.comunicavet.repository.CountryRepository;
import com.projetointegrador.comunicavet.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/countries")
@CrossOrigin("*")
public class CountryController {
    @Autowired
    private CountryRepository repository;

    @GetMapping
    public ResponseEntity<ApiResponse<Iterable<?>>> getAll() {
        Iterable<Country> countries = repository.findAll();

        return ResponseEntity.ok(new ApiResponse<>(false, "Lista de países", countries));
    }
}

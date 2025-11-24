package com.projetointegrador.comunicavet.controller;

import com.projetointegrador.comunicavet.dto.location.LocationDTO;
import com.projetointegrador.comunicavet.dto.location.SearchLocationDTO;
import com.projetointegrador.comunicavet.service.externalApi.NominatimService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/location-apis")
public class LocationApiController {
    @Autowired
    private NominatimService service;

    @GetMapping("/city")
    public ResponseEntity<?> getByCity(@NotNull @RequestParam("name") String city) {
        return ResponseEntity.ok(service.getLocationByCity(city));
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(
            @NotNull @RequestBody SearchLocationDTO dto,
            @RequestParam(required = false) String format, @RequestParam(required = false) Integer limit) {

        try {
            return ResponseEntity.ok(
                    service.getLocationByAddress(dto, Optional.ofNullable(format), Optional.ofNullable(limit))
            );

        } catch (IllegalAccessException e) {
            return ResponseEntity.status(500).body("Erro acessar a API");
        }
    }

    @GetMapping("/reverse")
    public ResponseEntity<?> reverse(
            @NotNull @RequestBody LocationDTO dto,
            @RequestParam(required = false) String format) {

        return ResponseEntity.ok(
                service.getAddressByLocation(dto, Optional.ofNullable(format))
        );
    }
}

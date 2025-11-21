package com.projetointegrador.comunicavet.controller;

import com.projetointegrador.comunicavet.dto.petClinicHistory.PetClinicRegisterDTO;
import com.projetointegrador.comunicavet.model.PetClinicHistory;
import com.projetointegrador.comunicavet.service.PetClinicHistoryService;
import com.projetointegrador.comunicavet.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pet-clinic-history")
public class PetClinicHistoryController {

    @Autowired
    private PetClinicHistoryService petClinicHistoryService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> register(@RequestBody RegisterRequest request) {
        petClinicHistoryService.register(request.getClinicId(), request.getPetId());

        return new ResponseEntity<>(
                new ApiResponse<>(true, "Clínica registrada na Carteirinha", null),
                HttpStatus.CREATED
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteById(@PathVariable Long id) {
        petClinicHistoryService.deleteById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Registro da Carteirinha deletado", null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PetClinicRegisterDTO>> getById(@PathVariable Long id) {
        PetClinicRegisterDTO history = petClinicHistoryService.getById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Registro da Carteirinha", history));
    }

    @GetMapping("/pet/{petId}")
    public ResponseEntity<ApiResponse<List<PetClinicRegisterDTO>>> getByPet(@PathVariable Long petId) {
        List<PetClinicRegisterDTO> histories = petClinicHistoryService.getByPet(petId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Histórico da Carteirinha do Pet", histories));
    }

    // Classe interna para representar a requisição de registro
    public static class RegisterRequest {
        private Long clinicId;
        private Long petId;

        // Getters e Setters
        public Long getClinicId() {
            return clinicId;
        }

        public void setClinicId(Long clinicId) {
            this.clinicId = clinicId;
        }

        public Long getPetId() {
            return petId;
        }

        public void setPetId(Long petId) {
            this.petId = petId;
        }
    }
}
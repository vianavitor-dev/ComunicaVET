package com.projetointegrador.comunicavet.controller;

import com.projetointegrador.comunicavet.dto.pet.RecommendBasedOnPetDTO;
import com.projetointegrador.comunicavet.dto.pythonAi.AiResponseDTO;
import com.projetointegrador.comunicavet.service.externalApi.PythonAIService;
import com.projetointegrador.comunicavet.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ai")
public class PythonAIController {
    @Autowired
    private PythonAIService pythonAIService;

    @PostMapping("/recommendation")
    public ResponseEntity<ApiResponse<AiResponseDTO>> recommendation(@RequestBody RecommendBasedOnPetDTO dto) {
        var response = pythonAIService.getRecommendationFromAI(dto);

        return ResponseEntity.ok(new ApiResponse<>(false, "", response));
    }
}

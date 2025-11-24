package com.projetointegrador.comunicavet.service.externalApi;

import com.projetointegrador.comunicavet.dto.pet.PetDTO;
import com.projetointegrador.comunicavet.dto.pet.RecommendBasedOnPetDTO;
import com.projetointegrador.comunicavet.dto.pythonAi.AiResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PythonAIService {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${python.ai.host}")
    private String pythonHost;

    public AiResponseDTO getRecommendationFromAI(RecommendBasedOnPetDTO dto) {
        String url = pythonHost + "/api/recommend";

        AiResponseDTO response = restTemplate.postForObject(url, dto, AiResponseDTO.class);
        System.out.println(response);

        return response;
    }
}

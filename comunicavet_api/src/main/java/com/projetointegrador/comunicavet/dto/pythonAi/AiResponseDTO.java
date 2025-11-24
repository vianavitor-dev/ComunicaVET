package com.projetointegrador.comunicavet.dto.pythonAi;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.lang.reflect.Array;
import java.util.List;

public record AiResponseDTO (
    Analysis analysis,
    String costs,
    @JsonProperty("processing_time_ms") Double processingTimeMs,
    Recommendations[] recommendations,
    String source,
    boolean success,
    String timestamp,
    String[] tips
) {
    public record Analysis (
            @JsonProperty("age_category") String ageCategory,
            // @JsonProperty("risk_factors") String[] riskFactors,
            @JsonProperty("special_care_needed") boolean specialCareNeeded
    ) {}

    public record Recommendations (
            String type,
            String priority,
            String message,
            String reason
    ) {}
}

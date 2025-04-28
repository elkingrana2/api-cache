package com.challenge.cache.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CalculationRequest {
    @NotNull(message = "num1 must not be null")
    private Double num1;

    @NotNull(message = "num2 must not be null")
    private Double num2;
}

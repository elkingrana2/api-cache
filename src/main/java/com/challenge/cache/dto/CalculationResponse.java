package com.challenge.cache.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalculationResponse {
    private double sum;
    private double percentage;
    private double result;
    private boolean cachedPercentageUsed;
}

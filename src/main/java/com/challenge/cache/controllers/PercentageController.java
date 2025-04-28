package com.challenge.cache.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

import com.challenge.cache.dto.CalculationRequest;
import com.challenge.cache.dto.CalculationResponse;
import com.challenge.cache.dto.HistoryResponse;
import com.challenge.cache.services.CalculationService;
import com.challenge.cache.services.HistoryService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api")
@Tag(name = "Percentage API", description = "API for calculations with dynamic percentage")
@Slf4j
public class PercentageController {
    private final CalculationService calculationService;
    private final HistoryService historyService;
    public PercentageController(CalculationService calculationService, HistoryService historyService) {
        this.calculationService = calculationService;
        this.historyService = historyService;
    }

    @PostMapping("/calculate")
    @Operation(summary = "Calculate sum with dynamic percentage")
    public CalculationResponse calculate(@RequestBody @Valid CalculationRequest request) {
        
        CalculationResponse response = calculationService.calculateWithPercentage(
            request.getNum1(), 
            request.getNum2()
        );
        
        // Async log to history
        historyService.logRequest("/api/calculate", request.toString(), response.toString(), null);
        
        return response;
    }

    @GetMapping("/history")
    @Operation(summary = "Get request history paginated")
    public Page<HistoryResponse> getHistory(Pageable pageable) {
        return historyService.getAllHistory(pageable)
            .map(history -> {
                HistoryResponse response = new HistoryResponse();
                response.setId(history.getId());
                response.setEndpoint(history.getEndpoint());
                response.setParameters(history.getParameters());
                response.setResponse(history.getResponse());
                response.setError(history.getError());
                response.setTimestamp(history.getTimestamp());
                return response;
            });
    }
}

package com.challenge.cache.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.challenge.cache.dto.ErrorResponse;
import com.challenge.cache.excepcions.InternalServerException;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class ExternalPercentageService {
    RestTemplate restTemplate;
    private final String externalServiceUrl;
    private final boolean mockEnabled;
    private final double mockPercentage;
  

    public ExternalPercentageService(
            @Value("${external.service.percentage.url}") String externalServiceUrl,
            @Value("${external.service.percentage.mock-enabled}") boolean mockEnabled,
            @Value("${external.service.percentage.mock-value}") double mockPercentage) {
        this.externalServiceUrl = externalServiceUrl;
        this.mockEnabled = mockEnabled;
        this.mockPercentage = mockPercentage;
    }

    public double getPercentage() {
        log.info("Calling external percentage service: {}", externalServiceUrl);
        log.info("Mock enabled: {}", mockEnabled);
        if (mockEnabled) {
            return mockPercentage;
        }
        
        try {
            // In a real scenario, this would call the external service
            // For this example, we'll just return the mock value
            RestTemplate rt = new RestTemplate();
            return rt.getForObject(externalServiceUrl, Double.class);
        } catch (Exception e) {
            List<String> errors = List.of(e.getMessage());
            throw new InternalServerException(new ErrorResponse("Error calling external percentage service", errors));
        }
    }
}

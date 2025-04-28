package com.challenge.cache.services;

import java.util.List;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import com.challenge.cache.config.CacheConfig;
import com.challenge.cache.dto.CalculationResponse;
import com.challenge.cache.dto.ErrorResponse;
import com.challenge.cache.excepcions.InternalServerException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CalculationService {
    private final ExternalPercentageService percentageService;
    private final CacheManager cacheManager;

    public CalculationService(ExternalPercentageService percentageService, CacheManager cacheManager) {
        this.percentageService = percentageService;
        this.cacheManager = cacheManager;
    }

    /**
     * Calcula la suma de dos números y aplica un porcentaje obtenido de un servicio externo.
     * Si el servicio externo falla, intenta obtener el porcentaje de la caché.
     * @param num1
     * @param num2
     * @return
     */
    public CalculationResponse calculateWithPercentage(double num1, double num2) {
        double sum = num1 + num2;
        double percentage;
        boolean cachedUsed = false;
        try {
            percentage = getPercentage();
            double result = sum * (1 + percentage / 100);
        
            return new CalculationResponse(sum, percentage, result, cachedUsed);
        } catch (Exception e) {
            log.info("Error al obtener el porcentaje desde el servicio externo: {}", e.getMessage());
            // cuando falla el servicio externo, se intenta obtener el porcentaje de la caché
            org.springframework.cache.Cache cache = cacheManager.getCache(CacheConfig.PERCENTAGE_CACHE);
            if (cache != null) {
                Double cachedPercentage = cache.get("percentage", Double.class);
                if (cachedPercentage != null) {
                    percentage = cachedPercentage;
                    cachedUsed = true;

                    double result = sum * (1 + percentage / 100);
        
                    return new CalculationResponse(sum, percentage, result, cachedUsed);
                }
            }
            List<String> errors = List.of("Error al obtener el porcentaje desde el servicio externo y no hay valor en caché.");
            throw new InternalServerException(new ErrorResponse("Error al obtener el porcentaje y no hay valor en caché.",errors));
        }
        

        
    }

    public double getPercentage() {
        
        double percentage = percentageService.getPercentage();
        org.springframework.cache.Cache cache = cacheManager.getCache(CacheConfig.PERCENTAGE_CACHE);
        if (cache != null) {
            cache.put("percentage", percentage);
        }
        return percentage;
        
    }
}
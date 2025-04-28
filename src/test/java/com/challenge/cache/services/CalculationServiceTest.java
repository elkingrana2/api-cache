package com.challenge.cache.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.challenge.cache.config.CacheConfig;
import com.challenge.cache.dto.CalculationResponse;
import com.challenge.cache.dto.ErrorResponse;
import com.challenge.cache.excepcions.InternalServerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;


@ExtendWith(MockitoExtension.class)
class CalculationServiceTest {

    @Mock
    private ExternalPercentageService percentageService;

    @Mock
    private CacheManager cacheManager;

    @Mock
    private Cache cache;

    private CalculationService calculationService;

    @BeforeEach
    void setUp() {
        calculationService = new CalculationService(percentageService, cacheManager);
    }

    @Test
    void shouldCalculateSumWithExternalPercentage() {
        // Arrange
        double num1 = 10;
        double num2 = 20;
        double percentage = 10.0;

        when(percentageService.getPercentage()).thenReturn(percentage);

        // Act
        CalculationResponse response = calculationService.calculateWithPercentage(num1, num2);

        // Assert
        assertEquals(30, response.getSum());
        assertEquals(10, response.getPercentage());
        assertEquals(33, response.getResult());
        assertFalse(response.isCachedPercentageUsed());
    }

    @Test
    void shouldCalculateSumWithCachedPercentageWhenExternalFails() {
        // Arrange
        double num1 = 10;
        double num2 = 20;
        double cachedPercentage = 5.0;

        when(percentageService.getPercentage()).thenThrow(new RuntimeException("External Service Down"));
        when(cacheManager.getCache(CacheConfig.PERCENTAGE_CACHE)).thenReturn(cache);
        when(cache.get("percentage", Double.class)).thenReturn(cachedPercentage);

        // Act
        CalculationResponse response = calculationService.calculateWithPercentage(num1, num2);

        // Assert
        assertEquals(30, response.getSum());
        assertEquals(5.0, response.getPercentage());
        assertEquals(31.5, response.getResult());
        assertTrue(response.isCachedPercentageUsed());
    }

    @Test
    void shouldThrowInternalServerExceptionWhenExternalAndCacheFail() {
        // Arrange
        when(percentageService.getPercentage()).thenThrow(new RuntimeException("External Service Down"));
        when(cacheManager.getCache(CacheConfig.PERCENTAGE_CACHE)).thenReturn(null);

        // Act & Assert
        InternalServerException exception = assertThrows(InternalServerException.class,
                () -> calculationService.calculateWithPercentage(10, 20));

        ErrorResponse errorResponse = exception.getErrorResponse();
        assertEquals("Error al obtener el porcentaje y no hay valor en caché.", errorResponse.getMessage());
    }

    @Test
    void shouldGetAndCachePercentage() {
        // Arrange
        double percentage = 8.0;

        when(percentageService.getPercentage()).thenReturn(percentage);
        when(cacheManager.getCache(CacheConfig.PERCENTAGE_CACHE)).thenReturn(cache);

        // Act
        double returnedPercentage = calculationService.getPercentage();

        // Assert
        assertEquals(percentage, returnedPercentage);
        verify(cache).put("percentage", percentage);
    }
}
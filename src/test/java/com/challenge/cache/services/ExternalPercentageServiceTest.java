package com.challenge.cache.services;

import static org.junit.jupiter.api.Assertions.*;

import com.challenge.cache.dto.ErrorResponse;
import com.challenge.cache.excepcions.InternalServerException;
import org.junit.jupiter.api.Test;

class ExternalPercentageServiceTest {

    @Test
    void shouldReturnMockPercentageWhenEnabled() {
        // Arrange
        ExternalPercentageService service = new ExternalPercentageService(
                "http://fake-url",
                true,
                15.0
        );

        // Act
        double percentage = service.getPercentage();

        // Assert
        assertEquals(15.0, percentage);
    }

    @Test
    void shouldThrowInternalServerExceptionWhenExternalCallFails() {
        // Arrange
        ExternalPercentageService service = new ExternalPercentageService(
                "http://invalid-url",
                false,
                0.0
        );

        // Act & Assert
        InternalServerException exception = assertThrows(InternalServerException.class, service::getPercentage);
        ErrorResponse errorResponse = exception.getErrorResponse();
        assertEquals("Error calling external percentage service", errorResponse.getMessage());
    }
}
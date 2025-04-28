package com.challenge.cache.excepcions;

import com.challenge.cache.dto.ErrorResponse;
import lombok.Getter;

@Getter
public class InternalServerException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final transient ErrorResponse errorResponse;

    public InternalServerException(ErrorResponse errorResponse) {
        super(errorResponse != null ? errorResponse.getMessage() : null);
        this.errorResponse = errorResponse;
    }
}

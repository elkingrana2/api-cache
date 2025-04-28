package com.challenge.cache.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class HistoryResponse {
    private Long id;
    private String endpoint;
    private String parameters;
    private String response;
    private String error;
    private LocalDateTime timestamp;
} 


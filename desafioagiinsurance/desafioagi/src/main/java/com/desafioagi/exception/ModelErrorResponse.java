package com.desafioagi.exception;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ModelErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String message;
    private String path;
}


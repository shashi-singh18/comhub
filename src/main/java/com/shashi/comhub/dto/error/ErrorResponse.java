package com.shashi.comhub.dto.error;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(
        name = "Error Response",
        description = "Represents the standard error response returned by the API."
)
public class ErrorResponse {

    @Schema(
            description = "Timestamp when the error occurred",
            example = "2026-08-04T09:45:12"
    )
    private LocalDateTime timestamp;

    @Schema(
            description = "HTTP status code",
            example = "400"
    )
    private int status;

    @Schema(
            description = "HTTP status reason",
            example = "Bad Request"
    )
    private String error;

    @Schema(
            description = "Detailed error message",
            example = "Category name cannot be blank."
    )
    private String message;

    @Schema(
            description = "Validation errors for individual fields",
            example = "{\"name\":\"Category name cannot be blank\",\"description\":\"Category description cannot be blank\"}"
    )
    private Map<String, String> validationErrors;

    @Schema(
            description = "API endpoint where the error occurred",
            example = "/api/v1/categories"
    )
    private String path;
}
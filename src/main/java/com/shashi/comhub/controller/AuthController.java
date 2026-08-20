package com.shashi.comhub.controller;

import com.shashi.comhub.dto.auth.LoginRequest;
import com.shashi.comhub.dto.auth.RegisterRequest;
import com.shashi.comhub.dto.error.ErrorResponse;
import com.shashi.comhub.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(
        name = "User Management",
        description = "APIs for managing users."
)
public class AuthController {

    private static final Logger logger =
            LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Register a new user")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "User created successfully"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "User already exists",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> register(
            @Valid @RequestBody RegisterRequest request) {

        logger.info("Received request to create user. name={}", request.getName());

        String message = authService.register(request);

        logger.info("Returning response. User created successfully. name={}", request.getName());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(message);
    }

    @Operation(summary = "Login existing new user")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User login successfully"
            )
    })
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    public ResponseEntity<String> login(
            @Valid @RequestBody LoginRequest request) {

        logger.info(
                "Received request to login user. email={}",
                request.getEmail()
        );

        String token = authService.login(request);

        logger.info(
                "Returning JWT after successful login. email={}",
                request.getEmail()
        );

        return ResponseEntity.ok(token);
    }
}
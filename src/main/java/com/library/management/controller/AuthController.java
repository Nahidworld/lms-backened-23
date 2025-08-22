package com.library.management.controller;

import com.library.management.dto.request.LoginRequest;
import com.library.management.dto.request.RegisterRequest;
import com.library.management.dto.response.AuthResponse;
import com.library.management.entity.UserRole;
import com.library.management.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "🔐 Authentication")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(
        summary = "📝 Register a new user", 
        description = "Create a new user account. Use this endpoint first to create your test account, then login to get your JWT token."
    )
    @ApiResponses(value = {
            @ApiResponse(
                responseCode = "201", 
                description = "✅ User registered successfully",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AuthResponse.class),
                    examples = @ExampleObject(
                        name = "Successful Registration",
                        value = """
                        {
                          "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                          "type": "Bearer",
                          "id": 1,
                          "email": "testuser@example.com",
                          "username": "testuser",
                          "role": "USER"
                        }
                        """
                    )
                )
            ),
            @ApiResponse(responseCode = "400", description = "❌ Invalid input data"),
            @ApiResponse(responseCode = "409", description = "❌ User already exists")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "User registration details",
        content = @Content(
            mediaType = "application/json",
            examples = @ExampleObject(
                name = "Sample Registration",
                value = """
                {
                  "username": "testuser",
                  "name": "Test User",
                  "email": "testuser@example.com",
                  "password": "password123",
                  "role": "USER"
                }
                """
            )
        )
    )
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/register-admin")
    @Operation(
        summary = "👑 Register a new admin user", 
        description = "Create a new admin user account with elevated privileges."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "✅ Admin registered successfully"),
            @ApiResponse(responseCode = "400", description = "❌ Invalid input data"),
            @ApiResponse(responseCode = "409", description = "❌ User already exists")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Admin registration details",
        content = @Content(
            mediaType = "application/json",
            examples = @ExampleObject(
                name = "Sample Admin Registration",
                value = """
                {
                  "username": "admin",
                  "name": "Admin User",
                  "email": "admin@example.com",
                  "password": "admin123"
                }
                """
            )
        )
    )
    public ResponseEntity<AuthResponse> registerAdmin(@Valid @RequestBody RegisterRequest request) {
        // Override the role to Admin
        request.setRole(UserRole.ADMIN);
        AuthResponse response = authService.register(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @Operation(
        summary = "🔑 Login user", 
        description = "Authenticate user and get JWT token. Copy the token from the response and use it in the 'Authorize' button above to test protected endpoints."
    )
    @ApiResponses(value = {
            @ApiResponse(
                responseCode = "200", 
                description = "✅ Login successful - Copy the token and click 'Authorize' button above!",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AuthResponse.class),
                    examples = @ExampleObject(
                        name = "Successful Login",
                        value = """
                        {
                          "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0dXNlciIsImlhdCI6MTYzOTU2NzIwMCwiZXhwIjoxNjM5NTcwODAwfQ.signature",
                          "type": "Bearer",
                          "id": 1,
                          "email": "testuser@example.com",
                          "username": "testuser",
                          "role": "USER"
                        }
                        """
                    )
                )
            ),
            @ApiResponse(responseCode = "401", description = "❌ Invalid credentials")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Login credentials",
        content = @Content(
            mediaType = "application/json",
            examples = @ExampleObject(
                name = "Sample Login",
                value = """
                {
                  "username": "testuser",
                  "password": "password123"
                }
                """
            )
        )
    )
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}


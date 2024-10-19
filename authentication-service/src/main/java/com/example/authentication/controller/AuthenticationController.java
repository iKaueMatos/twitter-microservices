package com.example.authentication.controller;

import com.example.authentication.dto.request.AuthenticationRequest;
import com.example.authentication.dto.request.RegisterRequest;
import com.example.authentication.dto.response.ActivationCodeResponse;
import com.example.authentication.dto.response.AuthenticationResponse;
import com.example.authentication.service.AuthenticationService;
import com.example.authentication.service.TokenService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final TokenService tokenService;

    @PostMapping("/register")
    public ResponseEntity<ActivationCodeResponse> register(@Valid @RequestBody RegisterRequest request) {
        ActivationCodeResponse response = authenticationService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationRequest request) {
        AuthenticationResponse response = authenticationService.authenticate(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/activate")
    public ResponseEntity<ActivationCodeResponse> activate(
            @RequestParam @Pattern(regexp = "^\\w{8}-\\w{4}-\\w{4}-\\w{4}-\\w{12}$", message = "{activation.invalid}")
            String activationCode
    ) {
        ActivationCodeResponse response = authenticationService.activate(activationCode);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/validate/{jwt}")
    public ResponseEntity<String> isTokenValid(@PathVariable String jwt) {
        String response = tokenService.isTokenValid(jwt);
        return ResponseEntity.ok(response);
    }
}

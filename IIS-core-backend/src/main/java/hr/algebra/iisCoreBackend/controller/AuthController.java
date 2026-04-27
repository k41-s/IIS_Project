package hr.algebra.iisCoreBackend.controller;

import hr.algebra.iisCoreBackend.dto.AuthResponse;
import hr.algebra.iisCoreBackend.dto.LoginRequest;
import hr.algebra.iisCoreBackend.dto.RefreshRequest;
import hr.algebra.iisCoreBackend.dto.RegisterRequest;
import hr.algebra.iisCoreBackend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }
}
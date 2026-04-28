package hr.algebra.iisCoreBackend.service;

import hr.algebra.iisCoreBackend.dto.AuthResponse;
import hr.algebra.iisCoreBackend.dto.LoginRequest;
import hr.algebra.iisCoreBackend.dto.RefreshRequest;
import hr.algebra.iisCoreBackend.dto.RegisterRequest;
import hr.algebra.iisCoreBackend.model.Role;
import hr.algebra.iisCoreBackend.model.User;
import hr.algebra.iisCoreBackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Value("${jwt.access-expiration}")
    private long accessExpiration;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username is already taken");
        }

        var user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.User);

        userRepository.save(user);
        return generateAuthResponse(user.getUsername(), user.getRole().name());
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return generateAuthResponse(request.getUsername(), user.getRole().name());
    }

    public AuthResponse refreshToken(RefreshRequest request) {
        String username = jwtService.extractUsername(request.getRefreshToken());

        if (username != null && jwtService.isTokenValid(request.getRefreshToken(), username)) {
            var user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            return generateAuthResponse(username, user.getRole().name());
        }
        throw new RuntimeException("Invalid Refresh Token");
    }

    private AuthResponse generateAuthResponse(String username, String role) {
        Map<String, Object> extraClaims = Map.of("role", role);

        String accessToken = jwtService.generateToken(extraClaims, username, accessExpiration);
        String refreshToken = jwtService.generateToken(username, refreshExpiration);

        return new AuthResponse(accessToken, refreshToken);
    }
}

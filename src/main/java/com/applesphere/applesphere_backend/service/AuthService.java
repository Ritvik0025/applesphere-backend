package com.applesphere.applesphere_backend.service;

import com.applesphere.applesphere_backend.config.JwtUtil;
import com.applesphere.applesphere_backend.dto.AuthResponse;
import com.applesphere.applesphere_backend.dto.LoginRequest;
import com.applesphere.applesphere_backend.dto.RegisterRequest;
import com.applesphere.applesphere_backend.model.User;
import com.applesphere.applesphere_backend.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final RedisService redisService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil,
            AuthenticationManager authenticationManager,
            RedisService redisService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.redisService = redisService;
    }

    public AuthResponse register(RegisterRequest request) {

        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        // Create new user
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setVillage(request.getVillage());
        user.setDistrict(request.getDistrict());
        user.setState(request.getState());

        // BCrypt hash the password before saving
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Save to PostgreSQL
        userRepository.save(user);

        // Generate JWT token
        String token = jwtUtil.generateToken(user.getEmail());

        // Save token in Redis for 24 hours
        redisService.save("token:" + user.getEmail(), token, 1440);

        return new AuthResponse(
            token,
            user.getEmail(),
            user.getName(),
            user.getRole().name(),
            user.getPlan().name()
        );
    }

    public AuthResponse login(LoginRequest request) {

        // Spring Security validates email + password against DB
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
            )
        );

        // Find user in database
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("User not found"));

        // Generate new JWT token
        String token = jwtUtil.generateToken(user.getEmail());

        // Save token in Redis for 24 hours
        redisService.save("token:" + user.getEmail(), token, 1440);

        return new AuthResponse(
            token,
            user.getEmail(),
            user.getName(),
            user.getRole().name(),
            user.getPlan().name()
        );
    }

    public void logout(String email) {
        // Delete token from Redis immediately
        redisService.delete("token:" + email);
    }
}
package com.dealersautocenter.api.controller;

import com.dealersautocenter.api.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "JWT Authentication APIs")
public class AuthController {
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @PostMapping("/login")
    @Operation(summary = "Generate JWT token", description = "Generate JWT token for authentication (demo purposes)")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> loginRequest) {
        // Simple demo authentication - in real scenario, validate credentials
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");
        
        // Demo credentials for testing
        if ("admin".equals(username) && "password".equals(password)) {
            String token = jwtUtil.generateToken(username);
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("type", "Bearer");
            response.put("username", username);
            return ResponseEntity.ok(response);
        }
        
        Map<String, String> error = new HashMap<>();
        error.put("error", "Invalid credentials");
        return ResponseEntity.status(401).body(error);
    }
    
    @PostMapping("/generate-token")
    @Operation(summary = "Generate demo token", description = "Generate a demo JWT token without authentication")
    public ResponseEntity<Map<String, String>> generateDemoToken() {
        String token = jwtUtil.generateToken("demo-user");
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("type", "Bearer");
        response.put("username", "demo-user");
        response.put("message", "Use this token in Authorization header: Bearer <token>");
        return ResponseEntity.ok(response);
    }
}

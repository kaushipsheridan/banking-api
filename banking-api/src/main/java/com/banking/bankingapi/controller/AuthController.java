package com.banking.bankingapi.controller;

import com.banking.bankingapi.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody Map<String, String> request) {
        return ResponseEntity.ok(authService.register(
                request.get("username"),
                request.get("password")
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> request) {
        return ResponseEntity.ok(authService.login(
                request.get("username"),
                request.get("password")
        ));
    }
}
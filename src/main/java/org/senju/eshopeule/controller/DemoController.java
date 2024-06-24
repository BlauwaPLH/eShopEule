package org.senju.eshopeule.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.dto.request.LoginRequest;
import org.senju.eshopeule.repository.TokenRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/demo")
public class DemoController {

    private final TokenRepository tokenRepository;

    @PostMapping
    public ResponseEntity<Map<?, ?>> demo(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(
                Map.of("message", "OKKKKKKKKKKKKK!")
        );
    }

    @GetMapping("/csrf")
    public CsrfToken csrf(CsrfToken csrfToken) {
        return csrfToken;
    }

    @GetMapping("/admin")
    public String adminPage() {
        return "this is Admin page!";
    }

}

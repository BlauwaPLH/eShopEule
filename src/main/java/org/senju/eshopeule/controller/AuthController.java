package org.senju.eshopeule.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.dto.request.LoginRequest;
import org.senju.eshopeule.dto.request.RefreshTokenRequest;
import org.senju.eshopeule.dto.request.RegistrationRequest;
import org.senju.eshopeule.dto.response.LoginResponse;
import org.senju.eshopeule.dto.response.RefreshTokenResponse;
import org.senju.eshopeule.dto.response.RegistrationResponse;
import org.senju.eshopeule.exceptions.*;
import org.senju.eshopeule.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/p/v1/auth")
public class AuthController {

    private final AuthService authService;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping(path = "/signIn")
    public ResponseEntity<LoginResponse> signIn(@Valid @RequestBody final LoginRequest request) {
        LoginResponse response;
        try {
            response = authService.authenticate(request);
            response.setMessage("Login successful!");
            return ResponseEntity.ok(response);
        } catch (UserNotExistsException | LoginException ex) {
            logger.error(ex.getMessage());
            response = LoginResponse.builder().message(ex.getMessage()).build();
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(response);
        }
    }

    @PostMapping(path = "/signUp")
    public ResponseEntity<RegistrationResponse> signUp(@Valid @RequestBody final RegistrationRequest request) {
        RegistrationResponse response;
        try {
            response = authService.register(request);
            response.setMessage("Register successful!");
            return ResponseEntity.ok(response);
        } catch (UserAlreadyExistsException | SignUpException ex) {
            logger.error(ex.getMessage());
            response = RegistrationResponse.builder().message(ex.getMessage()).build();
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }
    }


    @PostMapping(path = "/refreshToken")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@Valid @RequestBody final RefreshTokenRequest request) {
        RefreshTokenResponse response;
        try {
            response = authService.refreshToken(request);
            return ResponseEntity.ok(response);
        } catch (RefreshTokenException ex) {
            response = RefreshTokenResponse.builder()
                    .message(ex.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}

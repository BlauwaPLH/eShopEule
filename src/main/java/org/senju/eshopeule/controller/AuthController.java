package org.senju.eshopeule.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.dto.request.*;
import org.senju.eshopeule.dto.response.*;
import org.senju.eshopeule.exceptions.*;
import org.senju.eshopeule.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/p/v1/auth")
public class AuthController {

    private final AuthService authService;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping(path = "/signIn")
    public ResponseEntity<? extends BaseResponse> signIn(@Valid @RequestBody final LoginRequest request) {
        try {
            LoginResponse response = authService.authenticate(request);
            return ResponseEntity.ok(response);
        } catch (NotFoundException | LoginException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.status(UNAUTHORIZED).body(new SimpleResponse(ex.getMessage()));
        }
    }

    @PostMapping(path = "/signUp")
    public ResponseEntity<? extends BaseResponse> signUp(@Valid @RequestBody final RegistrationRequest request) {
        try {
            RegistrationResponse response = authService.register(request);
            response.setMessage("Register successful!");
            return ResponseEntity.ok(response);
        } catch (ObjectAlreadyExistsException | SignUpException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }


    @PostMapping(path = "/refreshToken")
    public ResponseEntity<? extends BaseResponse> refreshToken(@Valid @RequestBody final RefreshTokenRequest request) {
        try {
            RefreshTokenResponse response = authService.refreshToken(request);
            return ResponseEntity.ok(response);
        } catch (RefreshTokenException ex) {
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }

    @PostMapping(path = "/verifySignUp")
    public ResponseEntity<? extends BaseResponse> verifySignUp(@Valid @RequestBody final VerifyRequest request) {
        try {
            VerifyResponse response = authService.verifyRegister(request);
            return ResponseEntity.ok(response);
        } catch (VerifyException ex) {
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }

    @PostMapping(path = "/resendVerifyCode")
    public ResponseEntity<? extends BaseResponse> resendVerifyCode(@Valid @RequestBody final ResendVerifyCodeRequest request) {
        try {
            authService.resendRegistrationVerifyCode(request);
            return ResponseEntity.ok(new SimpleResponse("Resend successfully!"));
        } catch (VerifyException ex) {
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }

    @PostMapping(path = "/resetPassword")
    public ResponseEntity<? extends BaseResponse> resetPassword(@Valid @RequestBody final ResetPasswordRequest request) {
        try {
            authService.resetPassword(request);
            return ResponseEntity.ok(new SimpleResponse("Reset password successfully!"));
        } catch (ChangePasswordException | NotFoundException ex) {
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }
}

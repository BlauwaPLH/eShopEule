package org.senju.eshopeule.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.dto.request.*;
import org.senju.eshopeule.dto.response.*;
import org.senju.eshopeule.exceptions.*;
import org.senju.eshopeule.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/p/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping(path = "/signIn")
    @Operation(summary = "Log In")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successfully",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = SimpleResponse.class)))
    })
    public ResponseEntity<? extends BaseResponse> signIn(@Valid @RequestBody final LoginRequest request) {
        try {
            return ResponseEntity.ok(authService.authenticate(request));
        } catch (NotFoundException | LoginException ex) {
            return ResponseEntity.status(UNAUTHORIZED).body(new SimpleResponse(ex.getMessage()));
        }
    }

    @PostMapping(path = "/signUp")
    @Operation(summary = "Sign up")
    public ResponseEntity<? extends BaseResponse> signUp(@Valid @RequestBody final RegistrationRequest request) {
        try {
            RegistrationResponse response = authService.register(request);
            response.setMessage("Register successful!");
            return ResponseEntity.ok(response);
        } catch (ObjectAlreadyExistsException | SignUpException ex) {
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }


    @PostMapping(path = "/refreshToken")
    @Operation(summary = "Refresh token")
    public ResponseEntity<? extends BaseResponse> refreshToken(@Valid @RequestBody final RefreshTokenRequest request) {
        try {
            RefreshTokenResponse response = authService.refreshToken(request);
            return ResponseEntity.ok(response);
        } catch (RefreshTokenException ex) {
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }

    @PostMapping(path = "/verifySignUp")
    @Operation(summary = "Verify sign up")
    public ResponseEntity<? extends BaseResponse> verifySignUp(@Valid @RequestBody final VerifyRequest request) {
        try {
            VerifyResponse response = authService.verifyRegister(request);
            return ResponseEntity.ok(response);
        } catch (VerifyException ex) {
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }

    @PostMapping(path = "/resendVerifyCode")
    @Operation(summary = "Resend verify code to email")
    public ResponseEntity<? extends BaseResponse> resendVerifyCode(@Valid @RequestBody final ResendVerifyCodeRequest request) {
        try {
            authService.resendRegistrationVerifyCode(request);
            return ResponseEntity.ok(new SimpleResponse("Resend successfully!"));
        } catch (VerifyException ex) {
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }

    @PostMapping(path = "/resetPassword")
    @Operation(summary = "Reset password")
    public ResponseEntity<? extends BaseResponse> resetPassword(@Valid @RequestBody final ResetPasswordRequest request) {
        try {
            authService.resetPassword(request);
            return ResponseEntity.ok(new SimpleResponse("Reset password successfully!"));
        } catch (ChangePasswordException | NotFoundException ex) {
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }
}

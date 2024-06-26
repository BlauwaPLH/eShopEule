package org.senju.eshopeule.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.dto.request.LoginRequest;
import org.senju.eshopeule.dto.request.RegistrationRequest;
import org.senju.eshopeule.dto.response.LoginResponse;
import org.senju.eshopeule.dto.response.RegistrationResponse;
import org.senju.eshopeule.exceptions.LoginException;
import org.senju.eshopeule.exceptions.SignUpException;
import org.senju.eshopeule.exceptions.UserAlreadyExistsException;
import org.senju.eshopeule.exceptions.UserNotExistsException;
import org.senju.eshopeule.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/p/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
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

    @PostMapping(path = "/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            authService.logout(auth.getName());
            logoutHandler.logout(request, response, auth);
        }
        return ResponseEntity.ok().body("Logout successfully!");
    }

}

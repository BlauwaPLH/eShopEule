package org.senju.eshopeule.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.dto.request.ChangePasswordRequest;
import org.senju.eshopeule.exceptions.ChangePasswordException;
import org.senju.eshopeule.exceptions.UserNotExistsException;
import org.senju.eshopeule.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/r/v1/user")
public class UserController {
    private final AuthService authService;
    private final SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping(path = "/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            authService.logout(auth.getName());
            logoutHandler.logout(request, response, auth);
        }
        return ResponseEntity.ok().body("Logout successfully!");
    }

    @PostMapping(path = "/changePassword")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest request, @AuthenticationPrincipal UserDetails currUser) {
        try {
            authService.changePassword(request, currUser);
            return ResponseEntity.ok(Collections.singletonMap("message", "Updated password!"));
        } catch (UserNotExistsException | ChangePasswordException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", ex.getMessage()));
        }
    }
}

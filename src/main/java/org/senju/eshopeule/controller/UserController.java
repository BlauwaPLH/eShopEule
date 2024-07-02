package org.senju.eshopeule.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.dto.request.ChangePasswordRequest;
import org.senju.eshopeule.dto.response.BaseResponse;
import org.senju.eshopeule.dto.response.SimpleResponse;
import org.senju.eshopeule.exceptions.ChangePasswordException;
import org.senju.eshopeule.exceptions.NotFoundException;
import org.senju.eshopeule.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/r/v1/user")
public class UserController {
    private final AuthService authService;
    private final SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping(path = "/logout")
    public ResponseEntity<? extends BaseResponse> logout(HttpServletRequest request, HttpServletResponse response) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            authService.logout(auth.getName());
            logoutHandler.logout(request, response, auth);
        }
        return ResponseEntity.ok(SimpleResponse.builder().message("Logout successfully!").build());
    }

    @PostMapping(path = "/changePassword")
    public ResponseEntity<? extends BaseResponse> changePassword(@Valid @RequestBody ChangePasswordRequest request, @AuthenticationPrincipal UserDetails currUser) {
        try {
            authService.changePassword(request, currUser);
            return ResponseEntity.ok(SimpleResponse.builder().message("Updated password!").build());
        } catch (NotFoundException | ChangePasswordException ex) {
            return ResponseEntity.badRequest().body(SimpleResponse.builder().message(ex.getMessage()).build());
        }
    }
}

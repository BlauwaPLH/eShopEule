package org.senju.eshopeule.service;

import org.senju.eshopeule.dto.request.ChangePasswordRequest;
import org.senju.eshopeule.dto.request.LoginRequest;
import org.senju.eshopeule.dto.request.RefreshTokenRequest;
import org.senju.eshopeule.dto.request.RegistrationRequest;
import org.senju.eshopeule.dto.response.LoginResponse;
import org.senju.eshopeule.dto.response.RefreshTokenResponse;
import org.senju.eshopeule.dto.response.RegistrationResponse;
import org.senju.eshopeule.exceptions.*;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthService {

    LoginResponse authenticate(LoginRequest request) throws UserNotExistsException, LoginException;

    RefreshTokenResponse refreshToken(RefreshTokenRequest request,UserDetails currUser) throws RefreshTokenException;

    RegistrationResponse register(RegistrationRequest request) throws SignUpException, UserAlreadyExistsException;

    void changePassword(ChangePasswordRequest request, UserDetails userDetails) throws ChangePasswordException, UserNotExistsException;
}

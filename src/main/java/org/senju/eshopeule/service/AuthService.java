package org.senju.eshopeule.service;

import org.senju.eshopeule.dto.request.*;
import org.senju.eshopeule.dto.response.LoginResponse;
import org.senju.eshopeule.dto.response.RefreshTokenResponse;
import org.senju.eshopeule.dto.response.RegistrationResponse;
import org.senju.eshopeule.dto.response.VerifyResponse;
import org.senju.eshopeule.exceptions.*;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthService {

    LoginResponse authenticate(LoginRequest request) throws UserNotExistsException, LoginException;

    RefreshTokenResponse refreshToken(RefreshTokenRequest request) throws RefreshTokenException;

    RegistrationResponse register(RegistrationRequest request) throws SignUpException, UserAlreadyExistsException;

    void logout(String identifier);

    void changePassword(ChangePasswordRequest request, UserDetails userDetails) throws ChangePasswordException, UserNotExistsException;

    VerifyResponse verifyRegister(VerifyRequest request) throws VerifyException;

    void resendRegistrationVerifyCode(ResendVerifyCodeRequest request) throws VerifyException;

    void resetPassword(ResetPasswordRequest request) throws ChangePasswordException, UserNotExistsException;
}

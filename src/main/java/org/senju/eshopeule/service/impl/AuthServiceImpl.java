package org.senju.eshopeule.service.impl;

import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.constant.enums.BootstrapRole;
import org.senju.eshopeule.constant.enums.JwtClaims;
import org.senju.eshopeule.dto.request.ChangePasswordRequest;
import org.senju.eshopeule.dto.request.LoginRequest;
import org.senju.eshopeule.dto.request.RefreshTokenRequest;
import org.senju.eshopeule.dto.request.RegistrationRequest;
import org.senju.eshopeule.dto.response.LoginResponse;
import org.senju.eshopeule.dto.response.RefreshTokenResponse;
import org.senju.eshopeule.dto.response.RegistrationResponse;
import org.senju.eshopeule.exceptions.*;
import org.senju.eshopeule.model.token.Token;
import org.senju.eshopeule.constant.enums.TokenType;
import org.senju.eshopeule.model.user.Role;
import org.senju.eshopeule.model.user.User;
import org.senju.eshopeule.repository.RoleRepository;
import org.senju.eshopeule.repository.UserRepository;
import org.senju.eshopeule.repository.TokenRepository;
import org.senju.eshopeule.service.AuthService;
import org.senju.eshopeule.service.InMemoryTokenService;
import org.senju.eshopeule.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

import static org.senju.eshopeule.constant.exceptionMessage.UserExceptionMsg.*;
import static org.senju.eshopeule.constant.exceptionMessage.AuthExceptionMsg.*;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;
    private final JwtUtil jwtUtil;
    private final InMemoryTokenService inMemoryTokenService;

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);



    @Override
    public LoginResponse authenticate(final LoginRequest request) throws UserNotExistsException, LoginException {
        final String username = userRepository.findUsernameByIde(request.getIdentifier())
                .orElseThrow(() -> new UserNotExistsException(USER_NOT_EXISTS_MSG));

        try {
            SecurityContextHolder.getContext().setAuthentication(
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(username, request.getPassword())));
        } catch (AuthenticationException ex) {
            throw new LoginException(LOGIN_ERROR_MSG);
        }

        final String accessToken = jwtUtil.generateAccessToken(
                Map.of(JwtClaims.TYPE.getClaimName(), TokenType.ACCESS_TOKEN.getTypeName()),
                username
        );
        inMemoryTokenService.save(username, Token.builder()
                .revoked(false)
                .token(accessToken)
                .identifier(username)
                .build());

        final String refreshToken = jwtUtil.generateRefreshToken(
                Map.of(JwtClaims.TYPE.getClaimName(), TokenType.REFRESH_TOKEN.getTypeName()),
                username
        );
        this.revokeRefreshTokenByIdentifier(username);
        tokenRepository.save(
                Token.builder()
                        .type(TokenType.REFRESH_TOKEN)
                        .token(refreshToken)
                        .identifier(username)
                        .revoked(false)
                        .build()
        );
        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

    }


    @Override
    public RefreshTokenResponse refreshToken(final RefreshTokenRequest request) throws RefreshTokenException {
        return null;
//        inMemoryTokenService.delete(currUser.getUsername());
//        String token = request.getRefreshToken();
//        String tokenType = jwtUtil.extractClaims(token, c -> c.get(JwtClaims.TYPE.getClaimName(), String.class));
//        boolean isTokenValid = tokenRepository.findByToken(token)
//                .map(t -> t.getIdentifier().equals(jwtUtil.extractUsername(token)) && !t.isRevoked())
//                .orElse(false);
//        if (jwtUtil.validateToken(token, currUser) && isTokenValid && tokenType.equals(TokenType.REFRESH_TOKEN.getTypeName())) {
//            this.revokeRefreshTokenByIdentifier(currUser.getUsername());
//            String refreshToken = jwtUtil.generateRefreshToken(
//                    Map.of(JwtClaims.TYPE.getClaimName(), TokenType.REFRESH_TOKEN.getTypeName()),
//                    currUser.getUsername()
//            );
//            String accessToken = jwtUtil.generateAccessToken(
//                    Map.of(JwtClaims.TYPE.getClaimName(), TokenType.REFRESH_TOKEN.getTypeName()),
//                    currUser.getUsername()
//            );
//            tokenRepository.save(
//                    Token.builder()
//                            .type(TokenType.REFRESH_TOKEN)
//                            .token(refreshToken)
//                            .identifier(currUser.getUsername())
//                            .build()
//            );
//            return RefreshTokenResponse.builder()
//                    .accessToken(accessToken)
//                    .refreshToken(refreshToken)
//                    .build();
//        } else throw new RefreshTokenException(REFRESH_TOKEN_ERROR_MSG);
    }

    @Override
    public RegistrationResponse register(final RegistrationRequest request)
            throws SignUpException, UserAlreadyExistsException {
        var users = userRepository.findAllByUsernameOrEmailOrPhoneNumber(request.getUsername(), request.getEmail(), request.getPhoneNumber());
        if (!users.isEmpty()) throw new UserAlreadyExistsException(USER_ALREADY_EXISTS_MSG);
        Role bootstrapRole = roleRepository.findByName(BootstrapRole.CUSTOMER.getRoleName()).orElseThrow(() -> new SignUpException(SIGNUP_ERROR_MSG));

        User newUser = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(bootstrapRole)
                .isEnabled(true)
                .isAccountNonLocked(true)
                .isAccountNonExpired(true)
                .isCredentialsNonExpired(true)
                .build();

        userRepository.save(newUser);
        return RegistrationResponse
                .builder()
                .message("CHECK EMAIL OR SMS VERIFICATION")
                .build();
    }

    @Override
    public void logout(String identifier) {
        logger.debug("LOGOUT.... {}", identifier);
        inMemoryTokenService.delete(identifier);
        tokenRepository.revokeRefreshTokenByIdentifier(identifier);
    }


    @Override
    public void changePassword(final ChangePasswordRequest request, final UserDetails userDetails) throws ChangePasswordException, UserNotExistsException {
        var connectedUser = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() -> new UserNotExistsException(USER_NOT_EXISTS_MSG));
        if (!passwordEncoder.matches(request.getOldPassword(), connectedUser.getPassword())) {
            throw new ChangePasswordException(CHANGE_PASSWORD_ERROR_MSG);
        }
        connectedUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(connectedUser);
    }

    private void revokeRefreshTokenByIdentifier(String identifier) {
        logger.debug("REVOKING REFRESH TOKEN BY IDENTIFIER");
        tokenRepository.revokeRefreshTokenByIdentifier(identifier);
    }
}

package org.senju.eshopeule.service.impl;

import io.jsonwebtoken.JwtException;
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
import org.senju.eshopeule.constant.enums.TokenType;
import org.senju.eshopeule.model.user.Role;
import org.senju.eshopeule.model.user.User;
import org.senju.eshopeule.repository.RedisRepository;
import org.senju.eshopeule.repository.RoleRepository;
import org.senju.eshopeule.repository.UserRepository;
import org.senju.eshopeule.service.AuthService;
import org.senju.eshopeule.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
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
    private final JwtUtil jwtUtil;
    private final RedisRepository<String> accessTokenRepository;
    private final RedisRepository<String> refreshTokenRepository;

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
        accessTokenRepository.save(username, accessToken);

        final String refreshToken = jwtUtil.generateRefreshToken(
                Map.of(JwtClaims.TYPE.getClaimName(), TokenType.REFRESH_TOKEN.getTypeName()),
                username
        );
        refreshTokenRepository.deleteByKey(username);
        refreshTokenRepository.save(username, refreshToken);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

    }


    @Override
    public RefreshTokenResponse refreshToken(final RefreshTokenRequest request) throws RefreshTokenException {
        final String oldRefreshToken = request.getRefreshToken();
        try {
            final String username = jwtUtil.extractUsername(oldRefreshToken);
            String tokenType = jwtUtil.extractClaims(oldRefreshToken, c -> c.get(JwtClaims.TYPE.getClaimName(), String.class));
            String storedRefreshToken = refreshTokenRepository.getByKey(username);
            if (storedRefreshToken == null) throw new RefreshTokenException(JWT_TOKEN_INVALID_ERROR_MSG);

            if (oldRefreshToken.equals(storedRefreshToken) && tokenType.equals(TokenType.REFRESH_TOKEN.getTypeName())) {
                accessTokenRepository.deleteByKey(username);
                refreshTokenRepository.deleteByKey(username);

                final String newRefreshToken = jwtUtil.generateRefreshToken(
                        Collections.singletonMap(JwtClaims.TYPE.getClaimName(), TokenType.REFRESH_TOKEN.getTypeName()),
                        username
                );
                final String newAccessToken = jwtUtil.generateAccessToken(
                        Collections.singletonMap(JwtClaims.TYPE.getClaimName(), TokenType.ACCESS_TOKEN.getTypeName()),
                        username
                );

                accessTokenRepository.save(username, newAccessToken);
                refreshTokenRepository.save(username, newRefreshToken);

                return RefreshTokenResponse.builder()
                        .message("Refresh token successfully")
                        .accessToken(newAccessToken)
                        .refreshToken(newRefreshToken)
                        .build();
            } else throw new RefreshTokenException(REFRESH_TOKEN_ERROR_MSG);
        } catch (JwtException ex) {
            throw new RefreshTokenException(ex.getMessage());
        }
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
        accessTokenRepository.deleteByKey(identifier);
        refreshTokenRepository.deleteByKey(identifier);
    }


    @Override
    public void changePassword(final ChangePasswordRequest request, final UserDetails userDetails) throws ChangePasswordException, UserNotExistsException {
        final String principal = userDetails.getUsername();
        var encodedPassword = userRepository.getEncodedPasswordByUsername(principal).orElseThrow(() -> new UserNotExistsException(USER_NOT_EXISTS_MSG));
        if (!passwordEncoder.matches(request.getOldPassword(), encodedPassword)) {
            throw new ChangePasswordException(CHANGE_PASSWORD_ERROR_MSG);
        }
        userRepository.updatePasswordByUsername(principal, passwordEncoder.encode(request.getNewPassword()));
    }
}

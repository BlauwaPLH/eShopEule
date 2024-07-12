package org.senju.eshopeule.service.impl;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.constant.enums.BootstrapRole;
import org.senju.eshopeule.constant.enums.JwtClaims;
import org.senju.eshopeule.constant.enums.NotificationType;
import org.senju.eshopeule.dto.NotificationDTO;
import org.senju.eshopeule.dto.request.*;
import org.senju.eshopeule.dto.response.LoginResponse;
import org.senju.eshopeule.dto.response.RefreshTokenResponse;
import org.senju.eshopeule.dto.response.RegistrationResponse;
import org.senju.eshopeule.dto.response.VerifyResponse;
import org.senju.eshopeule.exceptions.*;
import org.senju.eshopeule.constant.enums.TokenType;
import org.senju.eshopeule.model.user.Customer;
import org.senju.eshopeule.model.user.Role;
import org.senju.eshopeule.model.user.User;
import org.senju.eshopeule.repository.jpa.CustomerRepository;
import org.senju.eshopeule.repository.redis.RedisRepository;
import org.senju.eshopeule.repository.jpa.RoleRepository;
import org.senju.eshopeule.repository.jpa.UserRepository;
import org.senju.eshopeule.repository.projection.LoginInfoView;
import org.senju.eshopeule.service.AuthService;
import org.senju.eshopeule.service.NotificationService;
import org.senju.eshopeule.utils.JwtUtil;
import org.senju.eshopeule.utils.MessageUtil;
import org.senju.eshopeule.utils.StringGeneratorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

import static org.senju.eshopeule.constant.enums.NotificationType.ContentType.*;
import static org.senju.eshopeule.constant.enums.NotificationType.SendMethodType.EMAIL;
import static org.senju.eshopeule.constant.exceptionMessage.UserExceptionMsg.*;
import static org.senju.eshopeule.constant.exceptionMessage.AuthExceptionMsg.*;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final RoleRepository roleRepository;
    private final RedisRepository<String> refreshTokenRepository;
    private final RedisRepository<String> accessTokenRepository;
    private final RedisRepository<String> verificationCodeRepository;
    private final RedisRepository<RegistrationRequest> tmpUserRedisRepository;
    private final NotificationService emailNotificationService;
    private final JwtUtil jwtUtil;

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);


    @Override
    public LoginResponse authenticate(final LoginRequest request) {
        LoginInfoView loginInfoView = userRepository.getLoginInfoViewByIdentifier(request.getIdentifier())
                .orElseThrow(() -> new NotFoundException(USER_NOT_EXISTS_MSG));

        try {
            SecurityContextHolder.getContext().setAuthentication(
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(loginInfoView.getUsername(), request.getPassword())));
        } catch (AuthenticationException ex) {
            throw new LoginException(LOGIN_ERROR_MSG);
        }

        Pair<String, String> tokenPair = this.generateAuthToken(loginInfoView.getUsername());
        return LoginResponse.builder()
                .message("Login successful!")
                .accessToken(tokenPair.getFirst())
                .refreshToken(tokenPair.getSecond())
                .build();
    }

    @Override
    public RefreshTokenResponse refreshToken(final RefreshTokenRequest request) {
        final String oldRefreshToken = request.getRefreshToken();
        try {
            final String username = jwtUtil.extractUsername(oldRefreshToken);
            String tokenType = jwtUtil.extractClaims(oldRefreshToken, c -> c.get(JwtClaims.TYPE.getClaimName(), String.class));
            String storedRefreshToken = refreshTokenRepository.getByKey(username);
            if (storedRefreshToken == null) throw new RefreshTokenException(JWT_TOKEN_INVALID_ERROR_MSG);

            if (oldRefreshToken.equals(storedRefreshToken) && tokenType.equals(TokenType.REFRESH_TOKEN.getTypeName())) {
                Pair<String, String> tokenPair = this.generateAuthToken(username);
                return RefreshTokenResponse.builder()
                        .message("Refresh token successfully")
                        .accessToken(tokenPair.getFirst())
                        .refreshToken(tokenPair.getSecond())
                        .build();
            } else throw new RefreshTokenException(REFRESH_TOKEN_ERROR_MSG);
        } catch (JwtException ex) {
            throw new RefreshTokenException(ex.getMessage());
        }
    }

    @Override
    public RegistrationResponse register(final RegistrationRequest request) {
        boolean isExistingUser = userRepository.checkUserExistsWithUsernameOrEmail(request.getUsername(), request.getEmail());
        if (isExistingUser) throw new ObjectAlreadyExistsException(USER_ALREADY_EXISTS_MSG);

        tmpUserRedisRepository.save(request.getUsername(), request);

        final String verificationCode = StringGeneratorUtil.generateRandomCode(6);
        verificationCodeRepository.save(request.getUsername(), verificationCode);

        try {
            NotificationDTO notification = new NotificationDTO(new NotificationType(EMAIL, VERIFY_SIGNUP), verificationCode, request.getEmail());
            emailNotificationService.sendNotification(MessageUtil.buildMessage(notification));
        } catch (SendNotificationException ex) {
            throw new SignUpException(ex.getMessage());
        }

        return RegistrationResponse
                .builder()
                .message("CHECK EMAIL OR SMS VERIFICATION")
                .build();
    }


    @Override
    public VerifyResponse verifyRegister(final VerifyRequest request) {
        final String username = request.getUsername();
        final String verifyCode = request.getVerifyCode();
        final String storedVerifyCode = verificationCodeRepository.getByKey(username);

        if (storedVerifyCode == null || !storedVerifyCode.equals(verifyCode)) throw new VerifyException(VERIFICATION_CODE_ERROR);

        RegistrationRequest registrationRequest = tmpUserRedisRepository.getByKey(username);
        if (registrationRequest == null) throw new VerifyException(USER_NOT_EXISTS_MSG);

        boolean isExistingUser = userRepository.checkUserExistsWithUsernameOrEmail(registrationRequest.getUsername(), registrationRequest.getEmail());
        if (isExistingUser) throw new VerifyException(USER_ALREADY_EXISTS_MSG);

        Role bootstrapRole = roleRepository.findByName(BootstrapRole.CUSTOMER.getRoleName()).orElseThrow(() -> new VerifyException(SIGNUP_ERROR_MSG));

        User newUser = User.builder()
                .username(request.getUsername())
                .email(registrationRequest.getEmail())
                .phoneNumber(registrationRequest.getPhoneNumber())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .role(bootstrapRole)
                .isEnabled(true)
                .isAccountNonLocked(true)
                .isAccountNonExpired(true)
                .isCredentialsNonExpired(true)
                .build();

        newUser = userRepository.save(newUser);
        customerRepository.save(new Customer(newUser));

        verificationCodeRepository.deleteByKey(username);
        tmpUserRedisRepository.deleteByKey(username);

        return VerifyResponse.builder()
                .message("Verify Sign Up success")
                .build();
    }

    @Override
    public void resendRegistrationVerifyCode(final ResendVerifyCodeRequest request) {
        final String username = request.getUsername();
        final String email = request.getEmail();

        final var tmpUser = tmpUserRedisRepository.getByKey(username);
        if (tmpUser == null) throw new VerifyException(USER_NOT_EXISTS_MSG);

        final String newVerifyCode = StringGeneratorUtil.generateRandomCode(6);
        verificationCodeRepository.save(username, newVerifyCode);

        try {
            final NotificationType type = new NotificationType(EMAIL, VERIFY_SIGNUP);
            final NotificationDTO notification = new NotificationDTO(type, newVerifyCode, email);
            emailNotificationService.sendNotification(MessageUtil.buildMessage(notification));
        } catch (SendNotificationException ex) {
            throw new VerifyException(ex.getMessage());
        }
    }

    @Override
    public void logout(String identifier) {
        logger.debug("LOGOUT.... {}", identifier);
        accessTokenRepository.deleteByKey(identifier);
        refreshTokenRepository.deleteByKey(identifier);
    }

    @Override
    public void changePassword(final ChangePasswordRequest request, final UserDetails userDetails) {
        final String principal = userDetails.getUsername();
        var encodedPassword = userRepository.getEncodedPasswordByUsername(principal).orElseThrow(() -> new NotFoundException(USER_NOT_EXISTS_MSG));
        if (!passwordEncoder.matches(request.getOldPassword(), encodedPassword)) {
            throw new ChangePasswordException(CHANGE_PASSWORD_ERROR_MSG);
        }
        userRepository.updatePasswordWithUsername(principal, passwordEncoder.encode(request.getNewPassword()));
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        final String identifier = request.getIdentifier();
        final String email = userRepository.getEmailByIde(identifier)
                .orElseThrow(() -> new NotFoundException(USER_NOT_EXISTS_MSG));
        final String newPassword = StringGeneratorUtil.generatePassword();

        try {
            NotificationType type = new NotificationType(EMAIL, FORGOT_PASSWORD);
            NotificationDTO notification = new NotificationDTO(type, newPassword, email);
            emailNotificationService.sendNotification(MessageUtil.buildMessage(notification));
            userRepository.updatePasswordWithEmail(email, passwordEncoder.encode(newPassword));
        } catch (SendNotificationException ex) {
            throw new ChangePasswordException(ex.getMessage());
        }
    }

    private Pair<String, String> generateAuthToken(String username) {
        accessTokenRepository.deleteByKey(username);
        refreshTokenRepository.deleteByKey(username);

        final String newAccessToken = jwtUtil.generateAccessToken(
                Collections.singletonMap(JwtClaims.TYPE.getClaimName(), TokenType.ACCESS_TOKEN.getTypeName()),
                username
        );
        accessTokenRepository.save(username, newAccessToken);

        final String newRefreshToken = jwtUtil.generateRefreshToken(
                Collections.singletonMap(JwtClaims.TYPE.getClaimName(), TokenType.REFRESH_TOKEN.getTypeName()),
                username
        );
        refreshTokenRepository.save(username, newRefreshToken);

        return Pair.of(newAccessToken, newRefreshToken);
    }
}

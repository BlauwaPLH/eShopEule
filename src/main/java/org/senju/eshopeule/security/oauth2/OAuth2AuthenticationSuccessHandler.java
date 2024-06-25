package org.senju.eshopeule.security.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.constant.enums.JwtClaims;
import org.senju.eshopeule.constant.enums.TokenType;
import org.senju.eshopeule.dto.response.LoginResponse;
import org.senju.eshopeule.model.token.Token;
import org.senju.eshopeule.repository.TokenRepository;
import org.senju.eshopeule.service.InMemoryTokenService;
import org.senju.eshopeule.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public final class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(OAuth2AuthenticationSuccessHandler.class);
    private final AuthenticationSuccessHandler delegate = new SavedRequestAwareAuthenticationSuccessHandler();
    private final UserRepositoryOAuth2UserHandler oAuth2UserHandler;
    private final ObjectMapper objectMapper;
    private final InMemoryTokenService inMemoryTokenService;
    private final JwtUtil jwtUtil;
    private final TokenRepository tokenRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (authentication instanceof OAuth2AuthenticationToken) {
            logger.debug("Received OAuth2AuthenticationToken");
            final String authorizedClientRegistrationId = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();
            final String principalName = authentication.getName();
            final String username = oAuth2UserHandler.apply(
                    authorizedClientRegistrationId,
                    (OAuth2User) authentication.getPrincipal()
            );
            if (username != null) {
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
                tokenRepository.revokeRefreshTokenByIdentifier(username);
                tokenRepository.save(
                        Token.builder()
                                .type(TokenType.REFRESH_TOKEN)
                                .token(refreshToken)
                                .identifier(username)
                                .revoked(false)
                                .build()
                );

                var authResponse = LoginResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                objectMapper.writeValue(response.getWriter(), authResponse);
            } else {
                logger.debug("username is null");
                logger.debug("principal's name: {}", authentication.getPrincipal());
            }
        }
        this.delegate.onAuthenticationSuccess(request, response, authentication);
    }
}

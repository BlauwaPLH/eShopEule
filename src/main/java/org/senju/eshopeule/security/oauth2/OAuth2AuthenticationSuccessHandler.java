package org.senju.eshopeule.security.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.constant.enums.JwtClaims;
import org.senju.eshopeule.constant.enums.TokenType;
import org.senju.eshopeule.dto.response.LoginResponse;
import org.senju.eshopeule.repository.redis.RedisRepository;
import org.senju.eshopeule.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final RedisRepository<String> accessTokenRepository;
    private final RedisRepository<String> refreshTokenRepository;

    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (authentication instanceof OAuth2AuthenticationToken) {
            logger.debug("Received OAuth2AuthenticationToken");
            final String authorizedClientRegistrationId = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();
            final String username = oAuth2UserHandler.apply(
                    authorizedClientRegistrationId,
                    (OAuth2User) authentication.getPrincipal()
            );
            if (username != null) {
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

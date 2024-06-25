package org.senju.eshopeule.security.oauth2.identityProvider.discord;

import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.security.oauth2.OAuth2TokenRequester;
import org.senju.eshopeule.security.oauth2.identityProvider.OAuth2Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public final class DiscordOAuth2Provider implements OAuth2Provider {

    private final OAuth2TokenRequester oAuth2TokenRequester;
    private final RestTemplate restTemplate;

    private static final Logger logger = LoggerFactory.getLogger(DiscordOAuth2Provider.class);
    private static final String DISCORD_USER_INFO_URL = "https://discordapp.com/api/users/@me";

    @Override
    public String getEmail(String authorizedClientRegistrationId, String principal) {
        String accessToken = oAuth2TokenRequester.requestAccessToken(authorizedClientRegistrationId, principal);
        logger.debug("Access token for DISCORD OAuth2: {}", accessToken);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        headers.set(HttpHeaders.ACCEPT, "application/json");
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<DiscordUserInfoResponse> response = restTemplate.exchange(
                DISCORD_USER_INFO_URL,
                HttpMethod.GET,
                httpEntity,
                DiscordUserInfoResponse.class
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            var userInfo = response.getBody();
            Assert.notNull(userInfo, "Discord user info is null");
            if (userInfo.isVerified() && userInfo.getEmail() != null) {
                return userInfo.getEmail();
            }
        }

        return null;
    }
}

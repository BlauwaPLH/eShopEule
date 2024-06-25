package org.senju.eshopeule.security.oauth2.identityProvider.github;

import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.security.oauth2.OAuth2TokenRequester;
import org.senju.eshopeule.security.oauth2.identityProvider.OAuth2Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@RequiredArgsConstructor
public final class GithubOAuth2Provider implements OAuth2Provider {

    private final OAuth2TokenRequester oAuth2TokenRequester;
    private final RestTemplate restTemplate;

    private static final Logger logger = LoggerFactory.getLogger(GithubOAuth2Provider.class);
    private static final String GITHUB_EMAIL_API_URL = "https://api.github.com/user/emails";

    @Override
    public String getEmail(String authorizedClientRegistrationId, String principal) {
        String accessToken = oAuth2TokenRequester.requestAccessToken(authorizedClientRegistrationId, principal);
        logger.debug("Access token for GITHUB OAuth2: {}", accessToken);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        headers.set(HttpHeaders.ACCEPT, "application/vnd.github+json");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<List<GithubEmailUserInfoResponse>> response = restTemplate.exchange(
                GITHUB_EMAIL_API_URL,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<>() {}
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            List<GithubEmailUserInfoResponse> emailUserInfoResponses = response.getBody();
            if (emailUserInfoResponses != null && !emailUserInfoResponses.isEmpty()) {
                for (var emailInfo: emailUserInfoResponses) {
                    if (emailInfo.isPrimary() && emailInfo.isVerified()) {
                        return emailInfo.getEmail();
                    }
                }
            }
        }


        return null;
    }


}

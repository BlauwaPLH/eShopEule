package org.senju.eshopeule.security.oauth2;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class OAuth2TokenRequester {

    private final OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager;

    private OAuth2AuthorizedClient authorize(String clientRegistrationId, String principal) {
        OAuth2AuthorizeRequest request = OAuth2AuthorizeRequest.withClientRegistrationId(clientRegistrationId)
                .principal(principal).build();
        return oAuth2AuthorizedClientManager.authorize(request);
    }

    public String requestAccessToken(String clientRegistrationId, String principal) {
        return authorize(clientRegistrationId, principal).getAccessToken().getTokenValue();
    }

    public String requestRefreshToken(String clientRegistrationId, String principal) {
        return Objects.requireNonNull(authorize(clientRegistrationId, principal).getRefreshToken()).getTokenValue();
    }
}

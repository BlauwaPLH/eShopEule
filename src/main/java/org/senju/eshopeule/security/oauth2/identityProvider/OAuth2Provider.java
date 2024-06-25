package org.senju.eshopeule.security.oauth2.identityProvider;

public interface OAuth2Provider {

    String getEmail(String authorizedClientRegistrationId, String principal);
}

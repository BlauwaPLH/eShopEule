package org.senju.eshopeule.security.oauth2.identityProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public final class DelegatingOAuth2Provider implements OAuth2Provider {

    private static final Logger logger = LoggerFactory.getLogger(DelegatingOAuth2Provider.class);
    private static final String suffixClassName = "OAuth2Provider";
    private final Map<String, OAuth2Provider> idToOAuth2Providers;


    public DelegatingOAuth2Provider(List<Class<? extends OAuth2Provider>> providers, ApplicationContext applicationContext) {
        this.idToOAuth2Providers = providers.stream()
                .collect(Collectors.toMap(op -> {
                    final String className = op.getSimpleName();
                    return className.substring(0, className.indexOf(suffixClassName)).toLowerCase();
                }, applicationContext::getBean));
    }

    @Override
    public String getEmail(String authorizedClientRegistrationId, String principal) {
        if (authorizedClientRegistrationId == null) {
            throw new IllegalArgumentException("authorizedClientRegistrationId cannot be null");
        }
        if (!idToOAuth2Providers.containsKey(authorizedClientRegistrationId)) {
            throw new IllegalArgumentException("authorizedClientRegistrationId " + authorizedClientRegistrationId + " does not exist");
        }
        return idToOAuth2Providers.get(authorizedClientRegistrationId)
                .getEmail(authorizedClientRegistrationId, principal);
    }
}

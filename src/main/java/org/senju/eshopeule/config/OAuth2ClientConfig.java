package org.senju.eshopeule.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.AuthenticatedPrincipalOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;

@Configuration
public class OAuth2ClientConfig {

    @Value("${security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    @Value("${security.oauth2.client.registration.google.client-secret}")
    private String googleClientSecret;

    @Value("${security.oauth2.client.registration.github.client-id}")
    private String githubClientId;

    @Value("${security.oauth2.client.registration.github.client-secret}")
    private String githubClientSecret;

    @Bean
    public OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager() {
        var authorizedClientManager = new AuthorizedClientServiceOAuth2AuthorizedClientManager(
                this.clientRegistrationRepository(),
                this.oAuth2AuthorizedClientService()
        );
        authorizedClientManager.setAuthorizedClientProvider(this.oAuth2AuthorizedClientProvider());
        return authorizedClientManager;
    }

    @Bean
    public OAuth2AuthorizedClientRepository oAuth2AuthorizedClientRepository() {
        return new AuthenticatedPrincipalOAuth2AuthorizedClientRepository(this.oAuth2AuthorizedClientService());
    }

    @Bean
    public OAuth2AuthorizedClientService oAuth2AuthorizedClientService() {
        return new InMemoryOAuth2AuthorizedClientService(this.clientRegistrationRepository());
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(
                this.googleClientRegistration(),
                this.githubClientRegistration()
        );
    }

    @Bean
    public OAuth2AuthorizedClientProvider oAuth2AuthorizedClientProvider() {
        return OAuth2AuthorizedClientProviderBuilder.builder()
                .authorizationCode()
                .clientCredentials()
                .refreshToken()
                .build();
    }


    private ClientRegistration githubClientRegistration() {
        return CommonOAuth2Provider.GITHUB.getBuilder("github")
                .clientId(githubClientId)
                .clientSecret(githubClientSecret)
                .build();
    }

    private ClientRegistration googleClientRegistration() {
        return CommonOAuth2Provider.GOOGLE.getBuilder("google")
                .clientId(googleClientId)
                .clientSecret(googleClientSecret)
                .build();
    }
}

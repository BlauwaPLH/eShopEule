package org.senju.eshopeule.config;

import org.senju.eshopeule.security.oauth2.identityProvider.DelegatingOAuth2Provider;
import org.senju.eshopeule.security.oauth2.identityProvider.OAuth2Provider;
import org.senju.eshopeule.security.oauth2.identityProvider.discord.DiscordOAuth2Provider;
import org.senju.eshopeule.security.oauth2.identityProvider.github.GithubOAuth2Provider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.AuthenticatedPrincipalOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

import java.util.List;

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

    @Value("${security.oauth2.client.registration.discord.client-id}")
    private String discordClientId;

    @Value("${security.oauth2.client.registration.discord.client-secret}")
    private String discordClientSecret;


    @Bean
    public OAuth2Provider delegatingOAuth2Provider(ApplicationContext applicationContext) {
        return new DelegatingOAuth2Provider(
                List.of(
                        DiscordOAuth2Provider.class,
                        GithubOAuth2Provider.class
                ),
                applicationContext
        );
    }


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
                this.githubClientRegistration(),
                this.discordClientRegistration()
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

    private ClientRegistration discordClientRegistration() {
        return ClientRegistration.withRegistrationId("discord")
                .clientName("Discord")
                .clientId(discordClientId)
                .clientSecret(discordClientSecret)
                .scope("email", "identify")
                .authorizationUri("https://discord.com/oauth2/authorize")
                .tokenUri("https://discord.com/api/oauth2/token")
                .userInfoUri("https://discordapp.com/api/users/@me")
                .redirectUri("{baseUrl}/{action}/oauth2/code/{registrationId}")
                .userNameAttributeName("username")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .build();
    }

    private ClientRegistration githubClientRegistration() {
        return ClientRegistration.withRegistrationId("github")
                .clientName("Github")
                .clientId(githubClientId)
                .clientSecret(githubClientSecret)
                .scope("user:email")
                .authorizationUri("https://github.com/login/oauth/authorize")
                .tokenUri("https://github.com/login/oauth/access_token")
                .userInfoUri("https://api.github.com/user")
                .redirectUri("{baseUrl}/{action}/oauth2/code/{registrationId}")
                .userNameAttributeName("id")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .build();
    }



    private ClientRegistration googleClientRegistration() {
        return CommonOAuth2Provider.GOOGLE.getBuilder("google")
                .clientId(googleClientId)
                .clientSecret(googleClientSecret)
                .build();
    }
}

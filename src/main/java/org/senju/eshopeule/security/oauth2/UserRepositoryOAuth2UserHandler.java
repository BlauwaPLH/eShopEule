package org.senju.eshopeule.security.oauth2;

import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.constant.enums.BootstrapRole;
import org.senju.eshopeule.model.user.User;
import org.senju.eshopeule.repository.jpa.RoleRepository;
import org.senju.eshopeule.repository.jpa.UserRepository;
import org.senju.eshopeule.security.oauth2.identityProvider.OAuth2Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.function.BiFunction;


@Component
@RequiredArgsConstructor
public final class UserRepositoryOAuth2UserHandler implements BiFunction<String, OAuth2User, String> {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final OAuth2Provider delegatingOAuth2Provider;
    private static final Logger logger = LoggerFactory.getLogger(UserRepositoryOAuth2UserHandler.class);


    @Override
    public String apply(String authorizedClientRegistrationId, OAuth2User oAuth2User) {
        String email;
        if (oAuth2User instanceof OidcUser oidcUser) {
            logger.debug("If is a {}", OidcUser.class.getName());
            email = oidcUser.getEmail();
        } else {
            logger.debug("It is not an OIDC user: {}", OAuth2User.class.getName());
            email = oAuth2User.getAttribute("email");
        }

        if (email == null) {
            email = delegatingOAuth2Provider.getEmail(authorizedClientRegistrationId, oAuth2User.getName());
        }

        if (email != null) {
            String username = userRepository.getUsernameByEmail(email).orElse(null);
            if (username == null) {
                username = email.split("@")[0];
                final var newUser = userRepository.save(
                        User.builder()
                                .username(username)
                                .role(roleRepository.findByName(BootstrapRole.CUSTOMER.getRoleName()).orElse(null))
                                .email(email)
                                .isEnabled(true)
                                .isAccountNonLocked(true)
                                .isAccountNonExpired(true)
                                .isCredentialsNonExpired(true)
                                .build()
                );
                username = newUser.getUsername();
            }
            return username;
        }

        return null;
    }

}

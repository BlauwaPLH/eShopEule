package org.senju.eshopeule.security.authProvider;


import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.exceptions.JwtAuthenticationException;
import org.senju.eshopeule.repository.RedisRepository;
import org.senju.eshopeule.security.JwtAuthenticationToken;
import org.senju.eshopeule.security.SimpleUserDetailsService;
import org.senju.eshopeule.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import static org.senju.eshopeule.constant.exceptionMessage.AuthExceptionMsg.JWT_TOKEN_INVALID_ERROR_MSG;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtUtil jwtUtil;
    private final SimpleUserDetailsService userDetailsService;
    private final RedisRepository<String> accessTokenRepository;

    private final UserDetailsChecker authenticationChecks = new SimplePreUserDetailsChecker();

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationProvider.class);

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Assert.isInstanceOf(JwtAuthenticationToken.class, authentication, "Only JwtAuthenticationToken is supported");
        final JwtAuthenticationToken auth = (JwtAuthenticationToken) authentication;
        final String accessToken = auth.getAccessToken();
        try {
            final String username = jwtUtil.extractUsername(accessToken);
            final String storedToken = accessTokenRepository.getByKey(username);
            if (storedToken == null || !storedToken.equals(accessToken)) {
                throw new JwtAuthenticationException(JWT_TOKEN_INVALID_ERROR_MSG);
            }
            UserDetails user = userDetailsService.loadUserDetailsByUsername(username);
            this.authenticationChecks.check(user);
            return new JwtAuthenticationToken(user, user.getAuthorities(), accessToken);
        } catch (JwtException e) {
            throw new JwtAuthenticationException(e.getMessage());
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private static class SimplePreUserDetailsChecker implements UserDetailsChecker {
        @Override
        public void check(UserDetails user) {
            if (!user.isAccountNonLocked()) {
                logger.debug("User account is locked");
                throw new LockedException(JwtAuthenticationProvider.class.getSimpleName() + " : User account is locked");
            } else if (!user.isEnabled()) {
                logger.debug("User account is disabled");
                throw new DisabledException(JwtAuthenticationProvider.class.getSimpleName() + " : User account is disabled");
            } else if (!user.isAccountNonExpired()) {
                logger.debug("User account is expired");
                throw new AccountExpiredException(JwtAuthenticationProvider.class.getSimpleName() + " : User account is expired");
            } else if (!user.isCredentialsNonExpired()) {
                logger.debug("User credentials is expired");
                throw new CredentialsExpiredException(JwtAuthenticationProvider.class.getSimpleName() + " : User credentials is expired");
            }
        }
    }
}

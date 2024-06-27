package org.senju.eshopeule.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.exceptions.JwtAuthenticationException;
import org.senju.eshopeule.security.JwtAuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.senju.eshopeule.constant.exceptionMessage.AuthExceptionMsg.JWT_MISSING_ERROR_MSG;

public final class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter  {

    private static final String DEFAULT_PROCESSES_URL = "/api/r/**";
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final SecurityContextRepository securityContextRepository = new RequestAttributeSecurityContextRepository();
    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
    private final AuthenticationFailureHandler failureHandler;


    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, AuthenticationEntryPoint restAuthenticationEntryPoint) {
        super(DEFAULT_PROCESSES_URL, authenticationManager);
        this.failureHandler = new AuthenticationEntryPointFailureHandler(restAuthenticationEntryPoint);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.error("{} {}", JwtAuthenticationException.class.getSimpleName(), JWT_MISSING_ERROR_MSG);
            throw new JwtAuthenticationException(JWT_MISSING_ERROR_MSG);
        }
        final String authToken = authHeader.substring(7);
        JwtAuthenticationToken authRequest = new JwtAuthenticationToken(authToken);
        return getAuthenticationManager().authenticate(authRequest);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        SecurityContext context = this.securityContextHolderStrategy.createEmptyContext();
        context.setAuthentication(authResult);
        this.securityContextHolderStrategy.setContext(context);
        this.securityContextRepository.saveContext(context, request, response);
        logger.debug("Set SecurityContextHolder to {}", authResult);
        chain.doFilter(request, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        this.securityContextHolderStrategy.clearContext();
        failureHandler.onAuthenticationFailure(request, response, failed);
    }
}

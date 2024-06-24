package org.senju.eshopeule.security.filter;

import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

//@Component
public class CsrfTokenLoggingFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(CsrfTokenLoggingFilter.class);

    @Override
    protected void doFilterInternal(
            @Nonnull HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull FilterChain filterChain) throws ServletException, IOException {
        CsrfToken csrfToken = (CsrfToken) request.getAttribute("_csrf");
        response.addHeader("X-CSRF-TOKEN", csrfToken.getToken());
        logger.info("CSRF Token: {} {} {}", csrfToken.getToken(), csrfToken.getHeaderName(), csrfToken.getParameterName());
        filterChain.doFilter(request, response);
    }
}

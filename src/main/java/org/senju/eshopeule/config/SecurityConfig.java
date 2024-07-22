package org.senju.eshopeule.config;

import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.security.SimpleUserDetailsService;
import org.senju.eshopeule.security.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

import static org.senju.eshopeule.constant.enums.BootstrapPerm.*;
import static org.senju.eshopeule.constant.pattern.RoutePattern.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler;
    private final AuthenticationProvider jwtAuthenticationProvider;
    private final SimpleUserDetailsService userDetailsService;
    private final AuthenticationEntryPoint restAuthenticationEntryPoint;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
//                .csrf(c -> c.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .cors(c -> {
                    CorsConfigurationSource source = request -> {
                        CorsConfiguration config = new CorsConfiguration();
                        config.setAllowedOrigins(List.of("*"));
                        config.setAllowedHeaders(List.of("*"));
                        config.setAllowedMethods(List.of("*"));
                        return config;
                    };
                    c.configurationSource(source);
                })
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .addFilterAfter(csrfTokenLoggingFilter, CsrfFilter.class)
                .addFilterBefore(new JwtAuthenticationFilter(authenticationManager(http), restAuthenticationEntryPoint), UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(c -> c.successHandler(oauth2AuthenticationSuccessHandler))
                .authorizeHttpRequests(c -> c
                        .requestMatchers(HttpMethod.GET, STAFF_API, ROLE_API, "/api/r/*/stat/brand", "/api/r/*/stat/order",
                                "/api/r/*/stat/prod/**", "/api/r/*/stat/cate/**",
                                "/api/r/*/stat/cus/age-range", "/api/r/*/stat/cus/age-group",
                                "/api/r/*/stat/cus/gender", "/api/r/*/stat/cus/active").hasAuthority(ADMIN_READ.getPermName())
                        .requestMatchers(HttpMethod.POST, STAFF_API, ROLE_API).hasAuthority(ADMIN_WRITE.getPermName())
                        .requestMatchers(HttpMethod.PUT, STAFF_API, ROLE_API).hasAuthority(ADMIN_WRITE.getPermName())
                        .requestMatchers(HttpMethod.DELETE, STAFF_API, ROLE_API).hasAuthority(ADMIN_WRITE.getPermName())

                        .requestMatchers(HttpMethod.GET, PROD_IMG_API, PROD_ATTR_API, PROD_META_API, PROD_OPTION_API, CATEGORY_API, BRAND_API, PRODUCT_API).hasAuthority(STAFF_READ.getPermName())
                        .requestMatchers(HttpMethod.POST, PROD_IMG_API, PROD_ATTR_API, PROD_META_API, PROD_OPTION_API, CATEGORY_API, BRAND_API, PRODUCT_API).hasAuthority(STAFF_WRITE.getPermName())
                        .requestMatchers(HttpMethod.PUT, PROD_IMG_API, PROD_ATTR_API, PROD_META_API, PROD_OPTION_API, CATEGORY_API, BRAND_API, PRODUCT_API).hasAuthority(STAFF_WRITE.getPermName())
                        .requestMatchers(HttpMethod.DELETE, PROD_IMG_API, PROD_ATTR_API, PROD_META_API, PROD_OPTION_API, CATEGORY_API, BRAND_API, PRODUCT_API).hasAuthority(STAFF_WRITE.getPermName())

                        .requestMatchers(HttpMethod.GET, CART_API, "/api/r/*/cm/**", "/api/r/v1/stat/cus/os",
                                "/api/r/v1/stat/cus/oi", "/api/r/v1/stat/cus/completed").hasAuthority(CUS_READ.getPermName())
                        .requestMatchers(HttpMethod.POST, CART_API, "/api/r/*/cm/**").hasAuthority(CUS_WRITE.getPermName())
                        .requestMatchers(HttpMethod.DELETE, CART_API).hasAuthority(CUS_WRITE.getPermName())

                        .requestMatchers("/api/r/*/order").hasAnyAuthority(CUS_READ.getPermName(), STAFF_READ.getPermName())
                        .requestMatchers("/api/r/*/order/cancel").hasAnyAuthority(CUS_WRITE.getPermName(), STAFF_WRITE.getPermName())
                        .requestMatchers("/api/r/*/order/m/all").hasAuthority(STAFF_READ.getPermName())
                        .requestMatchers("/api/r/*/order/history").hasAuthority(CUS_READ.getPermName())
                        .requestMatchers("/api/r/*/order/crt", "/api/r/*/order/ba", "/api/r/*/cm", "/api/r/*/rating/**").hasAuthority(CUS_WRITE.getPermName())
                        .requestMatchers("/api/r/*/order/m/complete", "/api/r/*/order/m/ship").hasAuthority(STAFF_WRITE.getPermName())


                        .requestMatchers(PUBLIC_API, "/swagger-ui/**", "/swagger-ui",
                                "/v3/api-docs/**", "/actuator/health/**").permitAll()
                        .anyRequest().authenticated()
                )
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
        AuthenticationManagerBuilder builder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        builder.authenticationProvider(daoAuthenticationProvider());
        builder.authenticationProvider(jwtAuthenticationProvider);
        return builder.build();
    }

    private AuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService());
        return provider;
    }


    private UserDetailsService userDetailsService() {
        return userDetailsService::loadUserDetailsByUsername;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

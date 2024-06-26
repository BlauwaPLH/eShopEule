package org.senju.eshopeule.config;

import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.constant.enums.BootstrapPerm;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler;
    private final Filter jwtAuthenticationFilter;



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
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(c -> c
                        .successHandler(oauth2AuthenticationSuccessHandler)
//                        .failureHandler(authenticationFailureHandler)
                )
                .authorizeHttpRequests(c -> c
                        .requestMatchers("/api/v1/demo/admin").hasAnyAuthority(BootstrapPerm.ADMIN_READ.getPermName(), BootstrapPerm.ADMIN_WRITE.getPermName())
                        .requestMatchers("/api/p/**").permitAll()
                        .anyRequest().authenticated()
                )
                .build();
    }
}

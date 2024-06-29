package org.senju.eshopeule.config;

import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.constant.enums.BootstrapPerm;
import org.senju.eshopeule.security.SimpleUserDetailsService;
import org.senju.eshopeule.security.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
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

@Configuration
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
                .oauth2Login(c -> c
                        .successHandler(oauth2AuthenticationSuccessHandler)
                )
                .authorizeHttpRequests(c -> c
                        .requestMatchers("/api/v1/demo/admin").hasAnyAuthority(BootstrapPerm.ADMIN_READ.getPermName(), BootstrapPerm.ADMIN_WRITE.getPermName())
                        .requestMatchers("/api/p/**").permitAll()
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

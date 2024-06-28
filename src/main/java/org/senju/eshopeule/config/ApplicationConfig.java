package org.senju.eshopeule.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.repository.PermissionRepository;
import org.senju.eshopeule.repository.RoleRepository;
import org.senju.eshopeule.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
@EnableJpaRepositories(basePackageClasses = {
        UserRepository.class,
        PermissionRepository.class,
        RoleRepository.class
})
public class ApplicationConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}

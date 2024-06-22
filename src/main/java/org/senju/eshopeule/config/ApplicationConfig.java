package org.senju.eshopeule.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "org.senju.eshopeule.repository.jpa")
@EnableRedisRepositories(basePackages = "org.senju.eshopeule.repository.redis")
public class ApplicationConfig {

}

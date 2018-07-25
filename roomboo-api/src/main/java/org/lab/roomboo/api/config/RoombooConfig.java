package org.lab.roomboo.api.config;

import org.lab.roomboo.api.security.MongoUserDetailService;
import org.lab.roomboo.domain.repository.ApiUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableMongoRepositories("org.lab.roomboo.domain.repository")
public class RoombooConfig {

	@Bean
	UserDetailsService userDetailsService(ApiUserRepository apiUserRepository) {
		return new MongoUserDetailService(apiUserRepository);
	}

	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}

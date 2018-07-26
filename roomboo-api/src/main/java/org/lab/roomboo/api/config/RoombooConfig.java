package org.lab.roomboo.api.config;

import org.lab.roomboo.core.RoombooCoreConfig;
import org.lab.roomboo.core.security.MongoUserDetailService;
import org.lab.roomboo.domain.repository.ApiUserRepository;
import org.lab.roomboo.mail.RoombooMailConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableMongoRepositories("org.lab.roomboo.domain.repository")
@EnableAsync
@Import({ RoombooCoreConfig.class, RoombooMailConfig.class })
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

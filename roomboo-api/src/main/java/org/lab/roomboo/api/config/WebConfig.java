package org.lab.roomboo.api.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
@EnableWebMvc
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		log.debug("Configuring message converters");
		Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
		builder.indentOutput(true); // .dateFormat(new SimpleDateFormat(Constants.Configuration.DateFormat));
		converters.add(new MappingJackson2HttpMessageConverter(builder.build()));
	}

	@Bean
	UserDetailsService userDetailsService(BCryptPasswordEncoder passwordEncoder) {
		log.debug("Creating user detail service");

		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
		User alice = new User("admin", passwordEncoder.encode("admin"),
			Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));
		User bob = new User("operator", passwordEncoder.encode("operator"),
			Arrays.asList(new SimpleGrantedAuthority("ROLE_OPERATOR")));
		manager.createUser(alice);
		manager.createUser(bob);
		return manager;
	}

	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}

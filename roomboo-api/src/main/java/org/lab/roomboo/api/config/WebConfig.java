package org.lab.roomboo.api.config;

import java.util.List;

import org.lab.roomboo.api.security.MongoUserDetailService;
import org.lab.roomboo.domain.repository.ApiUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.extern.slf4j.Slf4j;

//@Configuration
//@EnableWebMvc
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Slf4j
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		log.debug("Configuring message converters");
		Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
		builder.indentOutput(true); // .dateFormat(new SimpleDateFormat(Constants.Configuration.DateFormat));
		converters.add(new MappingJackson2HttpMessageConverter(builder.build()));
	}


}

package org.lab.roomboo.api.config;

import java.util.Properties;

import org.lab.roomboo.core.RoombooCoreConfig;
import org.lab.roomboo.core.security.MongoUserDetailService;
import org.lab.roomboo.domain.repository.ApiUserRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableAsync
@EnableScheduling
@Import(RoombooCoreConfig.class)
@Slf4j
public class RoombooConfig {

	@Bean
	UserDetailsService userDetailsService(ApiUserRepository apiUserRepository) {
		return new MongoUserDetailService(apiUserRepository);
	}

	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@ConditionalOnProperty(value = "app.env.mail.enabled", havingValue = "true", matchIfMissing = false)
	JavaMailSender getJavaMailSender(Environment env) {
		String smtpUsername = env.getProperty("app.env.mail.smtp.username");
		String smtpPassword = env.getProperty("app.env.mail.smtp.password");
		String smtpHost = env.getProperty("app.env.mail.smtp.host");
		Integer smtpPort = env.getProperty("app.env.mail.smtp.port", Integer.class);
		Boolean debug = env.getProperty("app.env.mail.smtp.debug", Boolean.class);

		log.info("Configuring mail sender using {}:{}", smtpHost, smtpPort);

		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(smtpHost);
		mailSender.setPort(smtpPort);
		mailSender.setUsername(smtpUsername);
		mailSender.setPassword(smtpPassword);
		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.debug", String.valueOf(debug));
		return mailSender;
	}

}

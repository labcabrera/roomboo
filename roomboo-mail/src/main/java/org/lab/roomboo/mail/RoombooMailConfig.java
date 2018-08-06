package org.lab.roomboo.mail;

import java.util.Properties;

import org.lab.roomboo.mail.listener.ReserveCreatedEmailListener;
import org.lab.roomboo.mail.listener.UserCreatedEmailListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import lombok.extern.slf4j.Slf4j;

@Configuration
@ComponentScan
@Slf4j
public class RoombooMailConfig {

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

	@Bean
	@ConditionalOnProperty(value = "app.env.mail.enabled", havingValue = "true", matchIfMissing = false)
	ReserveCreatedEmailListener reserveCreatedEmailListener() {
		return new ReserveCreatedEmailListener();
	}

	@Bean
	@ConditionalOnProperty(value = "app.env.mail.enabled", havingValue = "true", matchIfMissing = false)
	UserCreatedEmailListener userCreatedEmailListener() {
		return new UserCreatedEmailListener();
	}
}

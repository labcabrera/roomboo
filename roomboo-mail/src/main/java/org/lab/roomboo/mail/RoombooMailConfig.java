package org.lab.roomboo.mail;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@ComponentScan
public class RoombooMailConfig {

	@Value("${app.env.mail.smtp.username}")
	private String mailUsername;

	@Value("${app.env.mail.smtp.password}")
	private String mailPassword;

	@Value("${app.env.mail.smtp.host}")
	private String smtpHost;

	@Value("${app.env.mail.smtp.port}")
	private Integer smtpPort;

	@Value("${app.env.mail.smtp.debug:false}")
	private Boolean debug;

	@Bean
	@ConditionalOnProperty(value = "app.env.mail.enabled", havingValue = "true", matchIfMissing = false)
	JavaMailSender getJavaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(smtpHost);
		mailSender.setPort(smtpPort);
		mailSender.setUsername(mailUsername);
		mailSender.setPassword(mailPassword);
		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.debug", String.valueOf(debug));
		return mailSender;
	}
}

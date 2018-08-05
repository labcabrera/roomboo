package org.lab.roomboo.mail.listener;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;

public abstract class EmailListener<T extends ApplicationEvent> implements ApplicationListener<T> {

	@Autowired(required = false)
	protected JavaMailSender sender;

	@Autowired(required = false)
	protected TemplateEngine templateEngine;

	protected void sendHtmlMessage(String subject, String body, String to) throws MessagingException {
		MimeMessage mimeMessage = sender.createMimeMessage();
		MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
		message.setTo(to);
		message.setSubject(subject);
		message.setText(body, true);
		sender.send(mimeMessage);
	}
}

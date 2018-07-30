package org.lab.roomboo.mail.listener;

import java.util.Locale;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.lab.roomboo.core.model.event.ReserveCreatedEvent;
import org.lab.roomboo.domain.exception.EntityNotFoundException;
import org.lab.roomboo.domain.exception.RoombooException;
import org.lab.roomboo.domain.model.AppUser;
import org.lab.roomboo.domain.model.Reserve;
import org.lab.roomboo.domain.model.ReserveConfirmationToken;
import org.lab.roomboo.domain.repository.AppUserRepository;
import org.lab.roomboo.domain.repository.ReserveConfirmationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import lombok.extern.slf4j.Slf4j;

@Component
@Order(ReserveCreatedEvent.EventOrder.EmailNotification)
@Slf4j
public class ReserveCreatedEmailListener implements ApplicationListener<ReserveCreatedEvent> {

	@Autowired(required = false)
	private JavaMailSender sender;

	@Autowired(required = false)
	private TemplateEngine templateEngine;

	@Autowired
	private AppUserRepository appUserRepository;

	@Autowired
	private ReserveConfirmationTokenRepository tokenRepository;

	@Async
	@Override
	public void onApplicationEvent(ReserveCreatedEvent event) {
		if (sender == null) {
			log.debug("Email module is disabled.");
			return;
		}
		Reserve reserve = event.getReserve();
		try {
			// TODO check not already confirmed/cancelled
			String appUserId = reserve.getUser().getId();
			AppUser owner = appUserRepository.findById(appUserId)
				.orElseThrow(() -> new EntityNotFoundException(AppUser.class, appUserId));

			ReserveConfirmationToken token = tokenRepository.findByReserveId(reserve.getId(), reserve.getCreated())
				.orElseThrow(() -> new RoombooException("Can not recover confirmation token"));

			log.info("Sending booking notification mail");

			Context context = new Context(Locale.getDefault());
			context.setVariable("username", owner.getDisplayName());
			context.setVariable("reserve", reserve);
			context.setVariable("confirmationToken", token);
			String htmlContent = templateEngine.process("mail-reserve-created", context);

			sendHtmlMessage("Roomboo verification code", htmlContent, owner.getEmail());
			log.debug("Send confirmation email");
		}
		catch (Exception ex) {
			log.error("Mail notification error", ex);
		}
	}

	private void sendHtmlMessage(String subject, String body, String to) throws MessagingException {
		MimeMessage mimeMessage = sender.createMimeMessage();
		MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
		message.setTo(to);
		message.setSubject(subject);
		message.setText(body, true);
		sender.send(mimeMessage);
	}

}

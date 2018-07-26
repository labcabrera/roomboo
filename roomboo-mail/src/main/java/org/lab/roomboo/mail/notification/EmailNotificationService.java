package org.lab.roomboo.mail.notification;

import java.time.LocalDateTime;
import java.util.Locale;

import javax.mail.internet.MimeMessage;

import org.lab.roomboo.core.notification.BookingNotificationService;
import org.lab.roomboo.core.notification.BookingNotificationService.NotificationOrder;
import org.lab.roomboo.domain.exception.EntityNotFoundException;
import org.lab.roomboo.domain.model.Reserve;
import org.lab.roomboo.domain.model.ReserveConfirmationToken;
import org.lab.roomboo.domain.model.ReserveOwner;
import org.lab.roomboo.domain.repository.ReserveConfirmationTokenRepository;
import org.lab.roomboo.domain.repository.ReserveOwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import lombok.extern.slf4j.Slf4j;

@Service
@Order(NotificationOrder.EmailCreation)

@Slf4j
public class EmailNotificationService implements BookingNotificationService {

	@Autowired(required = false)
	private JavaMailSender sender;

	@Autowired(required = false)
	private TemplateEngine templateEngine;

	@Autowired
	private ReserveOwnerRepository reserveOwnerRepository;

	@Autowired
	private ReserveConfirmationTokenRepository tokenRepository;

	@Async
	@Override
	public void reserveCreated(Reserve reserve) {
		if (sender == null) {
			log.debug("Email module is disabled.");
			return;
		}
		try {
			String ownerId = reserve.getOwner().getId();
			ReserveOwner owner = reserveOwnerRepository.findById(ownerId)
				.orElseThrow(() -> new EntityNotFoundException(ReserveOwner.class, ownerId));
			ReserveConfirmationToken token = tokenRepository.findValidToken(reserve.getId(), LocalDateTime.now())
				.orElseGet(null);

			log.info("Sending booking notification mail");

			Context context = new Context(Locale.getDefault());
			context.setVariable("username", owner.getDisplayName());
			context.setVariable("reserve", reserve);
			context.setVariable("confirmationToken", token);
			final String htmlContent = templateEngine.process("html/email-reserve-created.html", context);

			MimeMessage mimeMessage = sender.createMimeMessage();
			MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
			message.setTo(owner.getEmail());
			message.setSubject("Roomboo verification code");
			message.setText(htmlContent, true);

			sender.send(mimeMessage);

			System.out.println(htmlContent);
			System.out.println();
		}
		catch (Exception ex) {
			log.error("Mail notification error", ex);
		}
	}

}

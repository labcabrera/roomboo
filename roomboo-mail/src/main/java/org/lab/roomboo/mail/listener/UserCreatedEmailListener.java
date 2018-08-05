package org.lab.roomboo.mail.listener;

import java.util.Locale;

import org.lab.roomboo.core.model.event.AppUserCreatedEvent;
import org.lab.roomboo.core.model.event.listener.EventListenerOrder;
import org.lab.roomboo.domain.exception.RoombooException;
import org.lab.roomboo.domain.model.AppUser;
import org.lab.roomboo.domain.model.UserConfirmationToken;
import org.lab.roomboo.domain.repository.UserConfirmationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;

import lombok.extern.slf4j.Slf4j;

@Component
@Order(EventListenerOrder.UserCreated.EmailNotification)
@Slf4j
public class UserCreatedEmailListener extends EmailListener<AppUserCreatedEvent> {

	@Autowired
	private UserConfirmationTokenRepository repository;

	@Async
	@Override
	public void onApplicationEvent(AppUserCreatedEvent event) {
		if (sender == null || templateEngine == null) {
			log.debug("Email module is disabled.");
			return;
		}
		log.info("Sending user confirmation mail");
		AppUser user = event.getUser();

		try {
			// TODO check user status
			UserConfirmationToken token = repository.findByUserId(user.getId())
				.orElseThrow(() -> new RoombooException("Missing confirmation token"));

			Context context = new Context(Locale.getDefault());
			context.setVariable("user", user);
			context.setVariable("token", token);
			String htmlContent = templateEngine.process("mail-user-created", context);

			sendHtmlMessage("Roomboo verification code", htmlContent, user.getEmail());
			log.debug("Send confirmation email");
		}
		catch (Exception ex) {
			log.error("Mail notification error", ex);
		}
	}

}

package org.lab.roomboo.mail.listener;

import java.util.Locale;

import org.lab.roomboo.core.event.ReserveCreatedEvent;
import org.lab.roomboo.core.event.listener.EventListenerOrder;
import org.lab.roomboo.domain.exception.EntityNotFoundException;
import org.lab.roomboo.domain.exception.RoombooException;
import org.lab.roomboo.domain.model.AppUser;
import org.lab.roomboo.domain.model.Reserve;
import org.lab.roomboo.domain.model.ReserveConfirmationToken;
import org.lab.roomboo.domain.repository.AppUserRepository;
import org.lab.roomboo.domain.repository.ReserveConfirmationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.thymeleaf.context.Context;

import lombok.extern.slf4j.Slf4j;

@Order(EventListenerOrder.ReserveCreated.EmailNotification)
@Slf4j
public class ReserveCreatedEmailListener extends EmailListener<ReserveCreatedEvent> {

	@Autowired
	private AppUserRepository appUserRepository;

	@Autowired
	private ReserveConfirmationTokenRepository tokenRepository;

	@Async
	@Override
	public void onApplicationEvent(ReserveCreatedEvent event) {
		if (sender == null || templateEngine == null) {
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

}

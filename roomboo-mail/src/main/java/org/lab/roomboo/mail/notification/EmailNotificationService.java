package org.lab.roomboo.mail.notification;

import org.lab.roomboo.core.notification.BookingNotificationService;
import org.lab.roomboo.domain.exception.EntityNotFoundException;
import org.lab.roomboo.domain.model.Reserve;
import org.lab.roomboo.domain.model.ReserveOwner;
import org.lab.roomboo.domain.repository.ReserveOwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailNotificationService implements BookingNotificationService {

	private static final String MESSAGE_TEMPLATE = "Code: %s\nReserve: %s\nReserve identifier: %s";

	@Autowired(required = false)
	private JavaMailSender sender;

	@Autowired
	private ReserveOwnerRepository reserveOwnerRepository;

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

			log.info("Sending booking notification mail");

			SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(owner.getEmail());
			message.setSubject("Roomboo verification code");
			//@formatter:off
			message.setText(String.format("Code: %s",
				reserve.getCode(),
				reserve.getName(),
				reserve.getId()));
			//@formatter:on
			sender.send(message);
		}
		catch (Exception ex) {
			log.error("Mail notification error", ex);
		}
	}

}

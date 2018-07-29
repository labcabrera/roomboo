package org.lab.roomboo.core.notification.impl;

import java.time.LocalDateTime;

import org.lab.roomboo.core.notification.ReserveCreatedProcessor;
import org.lab.roomboo.core.notification.ReserveCreatedProcessor.NotificationOrder;
import org.lab.roomboo.domain.exception.EntityNotFoundException;
import org.lab.roomboo.domain.model.Alert;
import org.lab.roomboo.domain.model.Alert.AlertType;
import org.lab.roomboo.domain.model.Reserve;
import org.lab.roomboo.domain.model.AppUser;
import org.lab.roomboo.domain.model.Room;
import org.lab.roomboo.domain.repository.AlertRepository;
import org.lab.roomboo.domain.repository.AppUserRepository;
import org.lab.roomboo.domain.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Order(NotificationOrder.AlertCreation)
@Slf4j
public class ReserveCreatedAlertProcessor implements ReserveCreatedProcessor {

	private final static String TEMPLATE_CREATED = "User %s created reserve '%s' (id: %s)\nRoom: %s\nStart :%s\nEnd: %s";

	@Autowired
	private AlertRepository alertRepository;

	@Autowired
	private AppUserRepository appUserRepository;

	@Autowired
	private RoomRepository roomRepository;

	/**
	 * Asynchronously generates an alert record associated with the reservation request.
	 */
	@Async
	@Override
	public void reserveCreated(Reserve reserve) {
		try {
			alertRepository.save(build(reserve));
		}
		catch (Exception ex) {
			log.error("Notification error", ex);
		}
	}

	private Alert build(Reserve reserve) {
		String appUserId = reserve.getUser().getId();
		AppUser owner = appUserRepository.findById(appUserId)
			.orElseThrow(() -> new EntityNotFoundException(AppUser.class, appUserId));
		String roomId = reserve.getRoom().getId();
		Room room = roomRepository.findById(roomId).orElseThrow(() -> new EntityNotFoundException(Room.class, appUserId));

		//@formatter:off
		return Alert.builder()
			.type(AlertType.INFO)
			.created(LocalDateTime.now())
			.subject("Reserve created")
			.message(String.format(TEMPLATE_CREATED,
				owner.getDisplayName(),
				reserve.getName(),
				reserve.getId(),
				room.getName(),
				reserve.getFrom(),
				reserve.getTo()))
			.build();
		//@formatter:on
	}

}

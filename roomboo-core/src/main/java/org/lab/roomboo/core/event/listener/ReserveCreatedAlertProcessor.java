package org.lab.roomboo.core.event.listener;

import java.time.LocalDateTime;

import org.lab.roomboo.core.event.ReserveCreatedEvent;
import org.lab.roomboo.domain.exception.EntityNotFoundException;
import org.lab.roomboo.domain.model.Alert;
import org.lab.roomboo.domain.model.Alert.AlertType;
import org.lab.roomboo.domain.model.AppUser;
import org.lab.roomboo.domain.model.Reserve;
import org.lab.roomboo.domain.model.Room;
import org.lab.roomboo.domain.repository.AlertRepository;
import org.lab.roomboo.domain.repository.AppUserRepository;
import org.lab.roomboo.domain.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Order(EventListenerOrder.ReserveCreated.AlertCreation)
@Slf4j
public class ReserveCreatedAlertProcessor implements ApplicationListener<ReserveCreatedEvent> {

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
	public void onApplicationEvent(ReserveCreatedEvent event) {
		try {
			alertRepository.save(build(event.getReserve()));
		}
		catch (Exception ex) {
			log.error("Notification error", ex);
		}
	}

	public void reserveCreated(Reserve reserve) {
	}

	private Alert build(Reserve reserve) {
		String appUserId = reserve.getUser().getId();
		AppUser owner = appUserRepository.findById(appUserId)
			.orElseThrow(() -> new EntityNotFoundException(AppUser.class, appUserId));
		String roomId = reserve.getRoom().getId();
		Room room = roomRepository.findById(roomId)
			.orElseThrow(() -> new EntityNotFoundException(Room.class, appUserId));

		//@formatter:off
		return Alert.builder()
			.alertType(AlertType.INFO)
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

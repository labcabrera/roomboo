package org.lab.roomboo.api.notification;

import java.time.LocalDateTime;

import org.lab.roomboo.domain.exception.EntityNotFoundException;
import org.lab.roomboo.domain.model.Alert;
import org.lab.roomboo.domain.model.Alert.AlertType;
import org.lab.roomboo.domain.model.Reserve;
import org.lab.roomboo.domain.model.ReserveOwner;
import org.lab.roomboo.domain.model.Room;
import org.lab.roomboo.domain.repository.AlertRepository;
import org.lab.roomboo.domain.repository.ReserveOwnerRepository;
import org.lab.roomboo.domain.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AlertNotificationService implements BookingNotificationService {

	private final static String TEMPLATE_CREATED = "User %s created reserve '%s' (id: %s)\nRoom: %s\nStart :%s\nEnd: %s";

	@Autowired
	private AlertRepository alertRepository;

	@Autowired
	private ReserveOwnerRepository ownerRepository;

	@Autowired
	private RoomRepository roomRepository;

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
		String ownerId = reserve.getOwner().getId();
		ReserveOwner owner = ownerRepository.findById(ownerId)
			.orElseThrow(() -> new EntityNotFoundException(ReserveOwner.class, ownerId));
		String roomId = reserve.getRoom().getId();
		Room room = roomRepository.findById(roomId).orElseThrow(() -> new EntityNotFoundException(Room.class, ownerId));

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

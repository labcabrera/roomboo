package org.lab.roomboo.core.service;

import java.util.List;

import org.lab.roomboo.core.model.BookingRequest;
import org.lab.roomboo.core.notification.BookingNotificationService;
import org.lab.roomboo.domain.model.AppUser;
import org.lab.roomboo.domain.model.Reserve;
import org.lab.roomboo.domain.model.Room;
import org.lab.roomboo.domain.repository.ReserveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BookingService {

	@Autowired
	private ReserveRepository reserveRepository;

	@Autowired
	private ReserveCodeGenerator codeGenerator;

	@Autowired
	private List<BookingNotificationService> notificationServices;

	public Reserve processBookingRequest(BookingRequest request) {
		// // TODO check dates
		Reserve reserve = new Reserve();
		reserve.setUser(AppUser.builder().id(request.getUserId()).build());
		reserve.setRoom(Room.builder().id(request.getRoomId()).build());
		reserve.setFrom(request.getFrom());
		reserve.setTo(request.getTo());
		reserve.setName(request.getName());
		reserve.setConfirmed(false);
		reserve.setCode(codeGenerator.generate());
		Reserve inserted = reserveRepository.insert(reserve);

		// TODO async
		notificationServices.forEach(x -> {
			log.debug("Processing notification with {}", x.getClass().getSimpleName());
			x.reserveCreated(inserted);
		});

		return inserted;
	}

}

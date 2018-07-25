package org.lab.roomboo.api.service;

import java.util.List;

import org.lab.roomboo.api.model.BookingRequest;
import org.lab.roomboo.api.notification.BookingNotificationService;
import org.lab.roomboo.domain.model.Reserve;
import org.lab.roomboo.domain.model.ReserveOwner;
import org.lab.roomboo.domain.model.Room;
import org.lab.roomboo.domain.repository.ReserveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
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
		reserve.setOwner(ReserveOwner.builder().id(request.getOwnerId()).build());
		reserve.setRoom(Room.builder().id(request.getRoomId()).build());
		reserve.setFrom(request.getFrom());
		reserve.setTo(request.getTo());
		reserve.setConfirmed(false);
		reserve.setCode(codeGenerator.randomCode());
		Reserve inserted = reserveRepository.insert(reserve);

		// TODO async
		notificationServices.forEach(x -> x.reserveCreated(inserted));

		return inserted;
	}

}

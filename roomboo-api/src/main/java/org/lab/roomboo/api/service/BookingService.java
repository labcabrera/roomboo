package org.lab.roomboo.api.service;

import org.lab.roomboo.api.model.BookingRequest;
import org.lab.roomboo.domain.exception.EntityNotFoundException;
import org.lab.roomboo.domain.model.Reserve;
import org.lab.roomboo.domain.model.ReserveOwner;
import org.lab.roomboo.domain.model.Room;
import org.lab.roomboo.domain.repository.ReserveOwnerRepository;
import org.lab.roomboo.domain.repository.ReserveRepository;
import org.lab.roomboo.domain.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

	@Autowired
	private RoomRepository roomRepository;

	@Autowired
	private ReserveOwnerRepository ownerRepository;

	@Autowired
	private ReserveRepository reserveRepository;

	@Autowired
	private ReserveCodeGenerator codeGenerator;

	public Reserve processBookingRequest(BookingRequest request) {
		Room room = roomRepository.findById(request.getRoomId())
			.orElseThrow(() -> new EntityNotFoundException(Room.class, request.getRoomId()));
		ReserveOwner owner = ownerRepository.findById(request.getOwnerId())
			.orElseThrow(() -> new EntityNotFoundException(ReserveOwner.class, request.getOwnerId()));
		// TODO check dates
		Reserve reserve = new Reserve();
		reserve.setOwner(owner);
		reserve.setRoom(room);
		reserve.setFrom(request.getFrom());
		reserve.setTo(request.getTo());
		reserve.setConfirmed(false);
		reserve.setCode(codeGenerator.randomCode());
		return reserveRepository.insert(reserve);

	}

}

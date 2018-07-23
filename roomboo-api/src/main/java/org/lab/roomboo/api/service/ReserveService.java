package org.lab.roomboo.api.service;

import java.time.LocalDateTime;

import org.lab.roomboo.domain.exception.ReserveOwnerNotFoundException;
import org.lab.roomboo.domain.exception.RoomNotFoundException;
import org.lab.roomboo.domain.model.ReserveOwner;
import org.lab.roomboo.domain.model.Room;
import org.lab.roomboo.domain.model.Reserve;
import org.lab.roomboo.domain.repository.ReserveOwnerRepository;
import org.lab.roomboo.domain.repository.RoomRepository;
import org.lab.roomboo.domain.repository.ReserveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReserveService {

	@Autowired
	private ReserveRepository roomReserverepository;

	@Autowired
	private ReserveOwnerRepository ownerRepository;

	@Autowired
	private RoomRepository roomRepository;

	@Autowired
	private ReserveCodeGenerator codeGenerator;

	// TODO wrap parameters
	public Reserve reserve(String roomId, String ownerId, LocalDateTime from, LocalDateTime to) {
		Room room = roomRepository.findById(roomId).orElseThrow(() -> new RoomNotFoundException(roomId));
		ReserveOwner owner = ownerRepository.findById(ownerId)
			.orElseThrow(() -> new ReserveOwnerNotFoundException(ownerId));
		// TODO check dates
		Reserve reserve = new Reserve();
		reserve.setOwner(owner);
		reserve.setRoom(room);
		reserve.setFrom(from);
		reserve.setTo(to);
		reserve.setConfirmed(false);
		reserve.setCode(codeGenerator.randomCode());
		return roomReserverepository.insert(reserve);
	}

}

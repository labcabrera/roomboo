package org.lab.roomboo.api.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.lab.roomboo.domain.exception.ReserveOwnerNotFoundException;
import org.lab.roomboo.domain.exception.RoomNotFoundException;
import org.lab.roomboo.domain.model.Reserve;
import org.lab.roomboo.domain.model.ReserveOwner;
import org.lab.roomboo.domain.model.Room;
import org.lab.roomboo.domain.repository.ReserveOwnerRepository;
import org.lab.roomboo.domain.repository.ReserveRepository;
import org.lab.roomboo.domain.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ReserveService {

	@Autowired
	private ReserveRepository roomReserverepository;

	@Autowired
	private ReserveOwnerRepository ownerRepository;

	@Autowired
	private RoomRepository roomRepository;

	@Autowired
	private ReserveCodeGenerator codeGenerator;

	@Autowired
	private MongoTemplate mongoTemplate;

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

	public List<Reserve> find(String roomId, LocalDate date) {
		LocalDateTime t0 = date.atStartOfDay();
		LocalDateTime t1 = t0.plusDays(1);
		Query query = new Query();
		query.addCriteria(Criteria.where("room.id").is(roomId));
		query.addCriteria(Criteria.where("from").gte(t0).lte(t1));
		if (log.isDebugEnabled()) {
			log.debug("Reserve date query: {}", query);
		}
		return mongoTemplate.find(query, Reserve.class);
	}

}

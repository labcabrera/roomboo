package org.lab.roomboo.api.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.lab.roomboo.api.model.RoomReserveRequest;
import org.lab.roomboo.domain.exception.ReserveOwnerNotFoundException;
import org.lab.roomboo.domain.exception.RoomNotFoundException;
import org.lab.roomboo.domain.model.ReserveOwner;
import org.lab.roomboo.domain.model.Room;
import org.lab.roomboo.domain.model.RoomReserve;
import org.lab.roomboo.domain.repository.ReserveOwnerRepository;
import org.lab.roomboo.domain.repository.RoomRepository;
import org.lab.roomboo.domain.repository.RoomReserveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

@Service
public class ReserveService {

	@Autowired
	private RoomReserveRepository roomReserverepository;

	@Autowired
	private ReserveOwnerRepository ownerRepository;

	@Autowired
	private RoomRepository roomRepository;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private ReserveCodeGenerator codeGenerator;

	public RoomReserve reserve(RoomReserveRequest request) {
		Room room = roomRepository.findById(request.getRoomId())
			.orElseThrow(() -> new RoomNotFoundException(request.getRoomId()));
		ReserveOwner owner = ownerRepository.findById(request.getOwnerId())
			.orElseThrow(() -> new ReserveOwnerNotFoundException(request.getOwnerId()));
		// TODO check dates
		RoomReserve reserve = new RoomReserve();
		reserve.setOwner(owner);
		reserve.setRoom(room);
		reserve.setFrom(request.getFrom());
		reserve.setTo(request.getTo());
		reserve.setConfirmed(false);
		reserve.setCode(codeGenerator.randomCode());
		return roomReserverepository.insert(reserve);
	}

	public Page<RoomReserve> search(RoomReserveRequest request, Pageable pageable) {
		Query query = new Query().with(pageable);
		if (StringUtils.isNotBlank(request.getOwnerId())) {
			query.addCriteria(Criteria.where("owner.id").is(request.getOwnerId()));
		}
		List<RoomReserve> list = mongoTemplate.find(query, RoomReserve.class);
		return PageableExecutionUtils.getPage(list, pageable, () -> mongoTemplate.count(query, RoomReserve.class));
	}

}

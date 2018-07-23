package org.lab.roomboo.service;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.lab.roomboo.api.model.RoomSearchRequest;
import org.lab.roomboo.model.Room;
import org.lab.roomboo.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

@Service
public class RoomService {

	@Autowired
	private RoomRepository repository;

	@Autowired
	private MongoTemplate mongoTemplate;

	public Optional<Room> findById(String id) {
		return repository.findById(id);
	}

	public Room insert(Room entity) {
		return repository.insert(entity);
	}

	public Room update(Room entity) {
		return repository.save(entity);
	}

	public void delete(String id) {
		repository.deleteById(id);
	}

	public Page<Room> search(RoomSearchRequest request, Pageable pageable) {
		Query query = new Query().with(pageable);
		if (StringUtils.isNotBlank(request.getBuildingId())) {
			query.addCriteria(Criteria.where("building.id").is(request.getBuildingId()));
		}
		if (request.getMinSize() != null) {
			query.addCriteria(Criteria.where("features.size").gte(request.getMinSize()));
		}
		if (request.getVideoRequired() != null && request.getVideoRequired()) {
			query.addCriteria(Criteria.where("features.video").is(Boolean.TRUE));
		}
		List<Room> list = mongoTemplate.find(query, Room.class);
		return PageableExecutionUtils.getPage(list, pageable, () -> mongoTemplate.count(query, Room.class));
	}

}

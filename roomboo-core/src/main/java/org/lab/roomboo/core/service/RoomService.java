package org.lab.roomboo.core.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.lab.roomboo.core.model.RoomSearchOptions;
import org.lab.roomboo.domain.model.Room;
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
	private MongoTemplate mongoTemplate;

	public Page<Room> findPageable(RoomSearchOptions options, Pageable pageable) {
		Query query = new Query().with(pageable);
		if (StringUtils.isNotBlank(options.getGroupId())) {
			query.addCriteria(Criteria.where("group.id").is(options.getGroupId()));
		}
		if (options.getMinSize() != null) {
			query.addCriteria(Criteria.where("features.size").gte(options.getMinSize()));
		}
		if (options.getVideoRequired() != null && options.getVideoRequired()) {
			query.addCriteria(Criteria.where("features.video").is(Boolean.TRUE));
		}
		List<Room> list = mongoTemplate.find(query, Room.class);
		return PageableExecutionUtils.getPage(list, pageable, () -> mongoTemplate.count(query, Room.class));
	}

}

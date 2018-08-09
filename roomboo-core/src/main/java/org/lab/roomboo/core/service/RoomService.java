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
		if (options.getMinSize() != null && options.getMinSize() > 0) {
			query.addCriteria(Criteria.where("features.size").gte(options.getMinSize()));
		}
		if (options.getVideoCallRequired() != null && options.getVideoCallRequired()) {
			query.addCriteria(Criteria.where("features.videoCall").is(Boolean.TRUE));
		}
		if (options.getConferenceCallRequired() != null && options.getConferenceCallRequired()) {
			query.addCriteria(Criteria.where("features.conferenceCall").is(Boolean.TRUE));
		}
		if (options.getBoardRequired() != null && options.getBoardRequired()) {
			query.addCriteria(Criteria.where("features.board").is(Boolean.TRUE));
		}
		if (options.getAudioRequired() != null && options.getAudioRequired()) {
			query.addCriteria(Criteria.where("features.audio").is(Boolean.TRUE));
		}
		if (options.getProjectorRequired() != null && options.getProjectorRequired()) {
			query.addCriteria(Criteria.where("features.projector").is(Boolean.TRUE));
		}
		List<Room> list = mongoTemplate.find(query, Room.class);
		return PageableExecutionUtils.getPage(list, pageable, () -> mongoTemplate.count(query, Room.class));
	}

}

package org.lab.roomboo.api.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.lab.roomboo.domain.model.Reserve;
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
	private MongoTemplate mongoTemplate;

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

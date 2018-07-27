package org.lab.roomboo.core.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
			log.debug("Reserve find at date query: {}", query);
		}
		return mongoTemplate.find(query, Reserve.class);
	}

	public Optional<Reserve> findAtDateTime(String roomId, LocalDateTime dateTime) {
		Query query = new Query();
		query.addCriteria(Criteria.where("room.id").is(roomId));
		query.addCriteria(Criteria.where("from").lte(dateTime));
		query.addCriteria(Criteria.where("to").gte(dateTime));
		query.addCriteria(Criteria.where("confirmed").ne(null));
		query.addCriteria(Criteria.where("cancelled").is(null));
		if (log.isDebugEnabled()) {
			log.debug("Reserve find at datetime query: {}", query);
		}
		Reserve reserve = mongoTemplate.findOne(query, Reserve.class);
		return Optional.ofNullable(reserve);
	}

	public Optional<Reserve> findNext(String roomId, LocalDateTime dateTime) {
		Query query = new Query();
		query.addCriteria(Criteria.where("room.id").is(roomId));
		query.addCriteria(Criteria.where("from").gte(dateTime));
		query.addCriteria(Criteria.where("confirmed").ne(null));
		query.addCriteria(Criteria.where("cancelled").is(null));
		if (log.isDebugEnabled()) {
			log.debug("Reserve find next query: {}", query);
		}
		return Optional.ofNullable(mongoTemplate.findOne(query, Reserve.class));
	}

}

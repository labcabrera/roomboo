package org.lab.roomboo.core.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.lab.roomboo.core.model.ReserveSearchOptions;
import org.lab.roomboo.domain.model.Reserve;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ReserveService {

	@Autowired
	private MongoTemplate mongoTemplate;

	public Page<Reserve> findPaginated(ReserveSearchOptions options, Pageable pageable) {
		Query query = new Query().with(pageable);
		if (StringUtils.isNotBlank(options.getRoomId())) {
			query.addCriteria(Criteria.where("room.id").is(options.getRoomId()));
		}
		if (StringUtils.isNotBlank(options.getUserId())) {
			query.addCriteria(Criteria.where("user.id").is(options.getUserId()));
		}
		if (!options.isIncludeUnconfirmed()) {
			query.addCriteria(Criteria.where("confirmed").ne(null));
		}
		if (!options.isIncludeUnconfirmed()) {
			query.addCriteria(Criteria.where("cancelled").is(null));
		}
		List<Reserve> list = mongoTemplate.find(query, Reserve.class);
		return PageableExecutionUtils.getPage(list, pageable, () -> mongoTemplate.count(query, Reserve.class));
	}

	public List<Reserve> find(String roomId, LocalDate date) {
		LocalDateTime t0 = date.atStartOfDay();
		LocalDateTime t1 = t0.plusDays(1);
		Query query = new Query();
		query.addCriteria(Criteria.where("room.id").is(roomId));
		query.addCriteria(Criteria.where("from").gte(t0).lte(t1));
		query.addCriteria(Criteria.where("confirmed").ne(null));
		query.addCriteria(Criteria.where("cancelled").is(null));
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

	public Optional<Reserve> findInRange(String roomId, LocalDateTime from, LocalDateTime to) {
		Query query = new Query();
		query.addCriteria(Criteria.where("room.id").is(roomId));
		query.addCriteria(Criteria.where("to").gt(from).orOperator(Criteria.where("from").lte(to)));
		query.addCriteria(Criteria.where("confirmed").ne(null));
		query.addCriteria(Criteria.where("cancelled").is(null));
		if (log.isDebugEnabled()) {
			log.debug("Reserve find in range query: {}", query);
		}
		return Optional.ofNullable(mongoTemplate.findOne(query, Reserve.class));
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

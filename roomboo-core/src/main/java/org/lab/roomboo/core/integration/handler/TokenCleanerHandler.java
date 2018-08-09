package org.lab.roomboo.core.integration.handler;

import java.time.LocalDateTime;

import org.lab.roomboo.domain.model.ReserveConfirmationToken;
import org.lab.roomboo.domain.model.UserConfirmationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TokenCleanerHandler {

	@Autowired
	private MongoTemplate mongoTemplate;

	public void execute() {
		log.info("Starting scheduled task");
		LocalDateTime now = LocalDateTime.now();
		Query query = new Query();
		query.addCriteria(Criteria.where("expiration").lt(now));
		mongoTemplate.remove(query, ReserveConfirmationToken.class);
		mongoTemplate.remove(query, UserConfirmationToken.class);
	}

}

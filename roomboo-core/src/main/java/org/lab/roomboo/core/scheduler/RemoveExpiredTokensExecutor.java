package org.lab.roomboo.core.scheduler;

import java.time.LocalDateTime;

import org.lab.roomboo.domain.model.ReserveConfirmationToken;
import org.lab.roomboo.domain.model.UserConfirmationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RemoveExpiredTokensExecutor {

	private static final String EVERY_DAY_3_AM = "0 0 3 * * *";

	@Autowired
	private MongoTemplate mongoTemplate;

	@Scheduled(cron = EVERY_DAY_3_AM)
	public void test() {
		log.info("Starting scheduled task");
		LocalDateTime now = LocalDateTime.now();
		Query query = new Query();
		query.addCriteria(Criteria.where("expiration").lt(now));
		mongoTemplate.remove(query, ReserveConfirmationToken.class);
		mongoTemplate.remove(query, UserConfirmationToken.class);
	}

}

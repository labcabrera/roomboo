package org.lab.roomboo.core.integration.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MongoHandler {

	@Autowired
	private MongoTemplate mongoTemplate;

	public Object save(Object source) {
		log.trace("Saving message {}", source);
		mongoTemplate.save(source);
		log.debug("Saved message {}", source);
		return source;
	}
}

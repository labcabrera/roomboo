package org.lab.roomboo.api.bootstrap;

import org.lab.roomboo.domain.model.Alert;
import org.lab.roomboo.domain.model.ReserveConfirmationToken;
import org.lab.roomboo.domain.model.UserConfirmationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class CleanupDataInitializer {

	@Autowired
	private MongoTemplate mongoTemplate;

	public void cleanup() {
		mongoTemplate.dropCollection(Alert.class);
		mongoTemplate.dropCollection(UserConfirmationToken.class);
		mongoTemplate.dropCollection(ReserveConfirmationToken.class);
	}

}

package org.lab.roomboo.core.integration.flow;

import org.lab.roomboo.core.integration.RoomboChannels;
import org.lab.roomboo.core.integration.handler.EmailSenderHandler;
import org.lab.roomboo.domain.model.Alert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.handler.LoggingHandler.Level;

@Configuration
public class AppFlowConfig {

	@Autowired
	private EmailSenderHandler emailSenderHandler;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Bean
	IntegrationFlow flowAlertInputChannel() {
		return IntegrationFlows
			.from(RoomboChannels.ALERT_IN)
			.log(Level.INFO, AppFlowConfig.class.getName(), m -> "Received alert message: " + m.getPayload())
			.handle(Alert.class, (request, headers) -> {
				mongoTemplate.save(request);
				return request;
			})
			.bridge()
			.get();
	}

	@Bean
	IntegrationFlow flowEmailOutput() {
		return IntegrationFlows
			.from(RoomboChannels.EMAIL_OUT)
			.log(Level.INFO, AppFlowConfig.class.getName(), m -> "Received mail message: " + m.getPayload())
			.handle(emailSenderHandler)
			.get();
	}
}

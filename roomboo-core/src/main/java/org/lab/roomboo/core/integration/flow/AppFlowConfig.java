package org.lab.roomboo.core.integration.flow;

import org.lab.roomboo.core.integration.Channels;
import org.lab.roomboo.core.integration.handler.EmailSenderHandler;
import org.lab.roomboo.core.integration.handler.MongoHandler;
import org.lab.roomboo.domain.model.Alert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.handler.LoggingHandler.Level;

@Configuration
public class AppFlowConfig {

	@Autowired
	private MongoHandler mongoHandler;

	@Autowired
	private EmailSenderHandler emailSenderHandler;

	@Bean
	IntegrationFlow flowAlertInputChannel() { //@formatter:off
		return IntegrationFlows
			.from(Channels.AlertInput)
			.log(Level.INFO, AppFlowConfig.class.getName(), m -> "Received alert message: " + m.getPayload())
			.handle(Alert.class, (request, headers) -> mongoHandler.save(request))
			.log(Level.INFO, "Processed alert notification")
			.get();
	} //@formatter:on

	@Bean
	IntegrationFlow flowEmailOutput() { //@formatter:off
		return IntegrationFlows
			.from(Channels.EmailOutput)
			.log(Level.INFO, AppFlowConfig.class.getName(), m -> "Received mail message: " + m.getPayload())
			.handle(emailSenderHandler)
			.get();
	} //@formatter:on
}

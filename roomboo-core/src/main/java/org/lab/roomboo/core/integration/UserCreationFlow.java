package org.lab.roomboo.core.integration;

import org.lab.roomboo.core.integration.RoombooIntegration.Channels;
import org.lab.roomboo.core.integration.handler.MongoHandler;
import org.lab.roomboo.core.integration.transformer.AlertSignUpTransformer;
import org.lab.roomboo.core.integration.transformer.SignUpAppUserTransformer;
import org.lab.roomboo.domain.model.Alert;
import org.lab.roomboo.domain.model.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.handler.LoggingHandler.Level;

@Configuration
public class UserCreationFlow {

	@Autowired
	private AlertSignUpTransformer alertSignUpTransformer;

	@Autowired
	private SignUpAppUserTransformer signUpUserTransformer;

	@Autowired
	private MongoHandler mongoHandler;

	@Bean
	IntegrationFlow userSignUpFlow() { //@formatter:off
		return IntegrationFlows
			.from(Channels.SignUpInput)
			.log(Level.INFO, "Processing user sign-up")
			.transform(signUpUserTransformer)
			.handle(AppUser.class, (request, headers) -> mongoHandler.save(request))
			.log(Level.INFO, UserCreationFlow.class.getName(), m -> "Received sign-up request: " + m.getPayload())
			.publishSubscribeChannel(c -> c.applySequence(false)
				.subscribe(f -> f
					.transform(alertSignUpTransformer)
					.channel(Channels.AlertInput))
				.subscribe(f -> f
					.channel(Channels.PrepareUserActivation)))
			.channel(Channels.SignUpOutput)
			.get();
	} //@formatter:on

	@Bean
	IntegrationFlow alertNotificationFlow() { //@formatter:off
		return IntegrationFlows
			.from(Channels.AlertInput)
			.log(Level.INFO, "Received alert notification")
			.handle(Alert.class, (request, headers) -> mongoHandler.save(request))
			.log(Level.INFO, "Processed alert notification")
			.get();
	} //@formatter:on

	@Bean
	IntegrationFlow userActivationFlow() { //@formatter:off
		return IntegrationFlows
			.from(Channels.PrepareUserActivation)
			.log(Level.INFO, "Received user pre-activation flow")
			.bridge()
			.get();
	} //@formatter:on

}

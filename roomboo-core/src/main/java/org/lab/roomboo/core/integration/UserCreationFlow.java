package org.lab.roomboo.core.integration;

import java.time.LocalDateTime;
import java.util.Map;

import org.lab.roomboo.core.integration.RoombooIntegration.Channels;
import org.lab.roomboo.core.integration.handler.MongoHandler;
import org.lab.roomboo.core.integration.router.RequestUserActivationRouter;
import org.lab.roomboo.core.integration.transformer.AlertSignUpTransformer;
import org.lab.roomboo.core.integration.transformer.SignUpAppUserTransformer;
import org.lab.roomboo.domain.model.Alert;
import org.lab.roomboo.domain.model.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.handler.GenericHandler;
import org.springframework.integration.handler.LoggingHandler.Level;
import org.springframework.messaging.MessageChannel;

@Configuration
public class UserCreationFlow {

	@Autowired
	private AlertSignUpTransformer alertSignUpTransformer;

	@Autowired
	private SignUpAppUserTransformer signUpUserTransformer;

	@Autowired
	private MongoHandler mongoHandler;

	@Autowired
	private RequestUserActivationRouter signUpConfimationRouter;
	
	@Qualifier(Channels.SignUpConfirmationAuto)
	@Autowired
	private MessageChannel channelSignUpConfirmationAuto;

	@Bean
	IntegrationFlow userSignUpFlow() { //@formatter:off
		return IntegrationFlows
			.from(Channels.SignUpInput)
			.log(Level.INFO, UserCreationFlow.class.getName(), m -> "Received sign-up request: " + m.getPayload())
			.transform(signUpUserTransformer)
			.handle(AppUser.class, (request, headers) -> mongoHandler.save(request))
			.publishSubscribeChannel(c -> c.applySequence(false)
				.subscribe(f -> f
					.transform(alertSignUpTransformer)
					.channel(Channels.AlertInput)))
			//.channel(Channels.SignUpOutput)
			.route(signUpConfimationRouter)
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
	IntegrationFlow flowSignUpConfirmationAuto() { //@formatter:off
		return IntegrationFlows
			.from(channelSignUpConfirmationAuto)
			.log(Level.INFO, "Received confirmation message")
			.handle(AppUser.class, new GenericHandler<AppUser>() {

				@Override
				public Object handle(AppUser payload, Map<String, Object> headers) {
					payload.setActivation(LocalDateTime.now());
					return payload;
				}
			})
			.handle(AppUser.class, (request, headers) -> mongoHandler.save(request))
			.channel(Channels.SignUpOutput)
			.get();
	} //@formatter:on

	@Bean
	IntegrationFlow flowSignUpConfirmationMail() { //@formatter:off
		return IntegrationFlows
			.from(Channels.SignUpConfirmationEmail)
			.log(Level.INFO, "Received message")
			.bridge()
			.get();
	} //@formatter:on

	@Bean
	IntegrationFlow flowSignUpError() { //@formatter:off
		return IntegrationFlows
			.from(Channels.SignUpError)
			.log(Level.INFO, "Received message")
			.bridge()
			.get();
	} //@formatter:on

}

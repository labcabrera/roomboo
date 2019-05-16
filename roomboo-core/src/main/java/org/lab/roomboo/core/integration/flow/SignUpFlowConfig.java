package org.lab.roomboo.core.integration.flow;

import java.time.LocalDateTime;

import org.lab.roomboo.core.integration.RoomboChannels;
import org.lab.roomboo.core.integration.handler.PayloadValidatorHandler;
import org.lab.roomboo.core.integration.handler.UserTokenConfirmationHandler;
import org.lab.roomboo.core.integration.handler.UserTokenGeneratorHandler;
import org.lab.roomboo.core.integration.router.UserActivationRouter;
import org.lab.roomboo.core.integration.transformer.UserActivationAlertTransformer;
import org.lab.roomboo.core.integration.transformer.UserActivationEmailTransformer;
import org.lab.roomboo.core.integration.transformer.UserRegisterAlertTransformer;
import org.lab.roomboo.core.integration.transformer.UserRegisterTransformer;
import org.lab.roomboo.domain.model.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.handler.LoggingHandler.Level;

@Configuration
public class SignUpFlowConfig {

	@Autowired
	private PayloadValidatorHandler validationHandler;

	@Autowired
	private UserRegisterAlertTransformer alertSignUpTransformer;

	@Autowired
	private UserRegisterTransformer signUpUserTransformer;

	@Autowired
	private UserActivationRouter signUpConfimationRouter;

	@Autowired
	private UserTokenGeneratorHandler userActivationTokenHandler;

	@Autowired
	private UserActivationEmailTransformer emailConfirmationTransformer;

	@Autowired
	private UserTokenConfirmationHandler userTokenConfirmationHandler;

	@Autowired
	private UserActivationAlertTransformer userActivationAlertTransformer;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Bean
	IntegrationFlow userSignUpFlow() {
		return IntegrationFlows
			.from(RoomboChannels.SIGN_UP_IN)
			.log(Level.INFO, SignUpFlowConfig.class.getName(), m -> "Received sign-up message: " + m.getPayload())
			.handle(validationHandler)
			.transform(signUpUserTransformer)
			.handle(AppUser.class, (request, headers) -> {
				mongoTemplate.save(request);
				return request;
			})
			.publishSubscribeChannel(c -> c.applySequence(false)
				.subscribe(f -> f
					.route(signUpConfimationRouter))
				.subscribe(f -> f
					.transform(alertSignUpTransformer)
					.channel(RoomboChannels.ALERT_IN)))
			.channel(RoomboChannels.SIGN_UP_OUT)
			.get();
	}

	@Bean
	IntegrationFlow flowSignUpConfirmationAuto() {
		return IntegrationFlows
			.from(RoomboChannels.SIGN_UP_CONFIRMATION_AUTO)
			.log(Level.INFO, SignUpFlowConfig.class.getName(), m -> "Received auto-confirmation message: " + m.getPayload())
			.handle(AppUser.class, (request, headers) -> {
				request.setActivation(LocalDateTime.now());
				mongoTemplate.save(request);
				return request;
			})
			.get();
	}

	@Bean
	IntegrationFlow flowSignUpConfirmationMail() {
		return IntegrationFlows
			.from(RoomboChannels.SIGN_UP_CONFIRMATION_EMAIL)
			.log(Level.INFO, SignUpFlowConfig.class.getName(), m -> "Received email confirmation message: " + m.getPayload())
			.handle(userActivationTokenHandler)
			.transform(emailConfirmationTransformer)
			.channel(RoomboChannels.EMAIL_OUT)
			.get();
	}

	@Bean
	IntegrationFlow flowUserNewTokenConfirmation() {
		return IntegrationFlows
			.from(RoomboChannels.USER_NEW_TOKEN_CONFIRMATION_IN)
			.log(Level.INFO, SignUpFlowConfig.class.getName(), m -> "Received new user token confirmation message: " + m.getPayload())
			.transform(payload -> mongoTemplate.findById(payload, AppUser.class))
			.publishSubscribeChannel(c -> c.applySequence(false)
				.subscribe(f -> f
					.route(signUpConfimationRouter)))
			.channel(RoomboChannels.USER_NEW_TOKEN_CONFIRMATION_OUT)
			.get();
	}

	@Bean
	IntegrationFlow flowUserTokenConfirmation() {
		return IntegrationFlows
			.from(RoomboChannels.USER_TOKEN_CONFIRMATION_IN)
			.log(Level.INFO, SignUpFlowConfig.class.getName(), m -> "Received user token confirmation: " + m.getPayload())
			.handle(userTokenConfirmationHandler)
			.publishSubscribeChannel(c -> c.applySequence(false)
				.subscribe(f -> f
					.transform(userActivationAlertTransformer)
					.channel(RoomboChannels.ALERT_IN)))
			.channel(RoomboChannels.USER_TOKEN_CONFIRMATION_OUT)
			.get();
	}

}

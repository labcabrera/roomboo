package org.lab.roomboo.core.integration.flow;

import org.lab.roomboo.core.integration.Channels;
import org.lab.roomboo.core.integration.handler.MongoHandler;
import org.lab.roomboo.core.integration.handler.UserActivationHandler;
import org.lab.roomboo.core.integration.handler.UserTokenConfirmationHandler;
import org.lab.roomboo.core.integration.handler.UserTokenGeneratorHandler;
import org.lab.roomboo.core.integration.router.UserActivationRouter;
import org.lab.roomboo.core.integration.transformer.EmailConfirmationTransformer;
import org.lab.roomboo.core.integration.transformer.PayloadValidatorHandler;
import org.lab.roomboo.core.integration.transformer.UserActivationAlertTransformer;
import org.lab.roomboo.core.integration.transformer.UserRegisterAlertTransformer;
import org.lab.roomboo.core.integration.transformer.UserRegisterTransformer;
import org.lab.roomboo.domain.model.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
	private MongoHandler mongoHandler;

	@Autowired
	private UserActivationRouter signUpConfimationRouter;

	@Autowired
	private UserActivationHandler userActivationHandler;

	@Autowired
	private UserTokenGeneratorHandler userActivationTokenHandler;

	@Autowired
	private EmailConfirmationTransformer emailConfirmationTransformer;

	@Autowired
	private UserTokenConfirmationHandler userTokenConfirmationHandler;

	@Autowired
	private UserActivationAlertTransformer userActivationAlertTransformer;

	@Bean
	IntegrationFlow userSignUpFlow() { //@formatter:off
		return IntegrationFlows
			.from(Channels.SignUpInput)
			.log(Level.INFO, SignUpFlowConfig.class.getName(), m -> "Received sign-up message: " + m.getPayload())
			.handle(validationHandler)
			.transform(signUpUserTransformer)
			.handle(AppUser.class, (request, headers) -> mongoHandler.save(request))
			.publishSubscribeChannel(c -> c.applySequence(false)
				.subscribe(f -> f
					.route(signUpConfimationRouter))
				.subscribe(f -> f
					.transform(alertSignUpTransformer)
					.channel(Channels.AlertInput)))
			.channel(Channels.SignUpOutput)
			.get();
	} //@formatter:on

	@Bean
	IntegrationFlow flowSignUpError() { //@formatter:off
		return IntegrationFlows
			.from(Channels.SignUpError)
			.log(Level.INFO, SignUpFlowConfig.class.getName(), m -> "Received sign-up error message: " + m.getPayload())
			.bridge()
			.get();
	} //@formatter:on

	@Bean
	IntegrationFlow flowSignUpConfirmationAuto() { //@formatter:off
		return IntegrationFlows
			.from(Channels.SignUpConfirmationAuto)
			.log(Level.INFO, SignUpFlowConfig.class.getName(), m -> "Received auto-confirmation message: " + m.getPayload())
			.handle(userActivationHandler)
			.handle(AppUser.class, (request, headers) -> mongoHandler.save(request))
			.get();
	} //@formatter:on

	@Bean
	IntegrationFlow flowSignUpConfirmationMail() { //@formatter:off
		return IntegrationFlows
			.from(Channels.SignUpConfirmationEmail)
			.log(Level.INFO, SignUpFlowConfig.class.getName(), m -> "Received email confirmation message: " + m.getPayload())
			.handle(userActivationTokenHandler)
			.transform(emailConfirmationTransformer)
			.channel(Channels.EmailOutput)
			.get();
	} //@formatter:on

	@Bean
	IntegrationFlow flowUserTokenConfirmation() { //@formatter:off
		return IntegrationFlows
			.from(Channels.UserTokenConfirmationInput)
			.log(Level.INFO, SignUpFlowConfig.class.getName(), m -> "Received user token confirmation: " + m.getPayload())
			.handle(userTokenConfirmationHandler)
			.channel(Channels.UserTokenConfirmationOutput)
			.publishSubscribeChannel(c -> c.applySequence(false)
				.subscribe(f -> f
					.transform(userActivationAlertTransformer)
					.channel(Channels.AlertInput)))
			.get();
	} //@formatter:on

}

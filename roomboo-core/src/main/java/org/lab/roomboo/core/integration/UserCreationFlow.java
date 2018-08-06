package org.lab.roomboo.core.integration;

import org.lab.roomboo.core.model.AppUserRegisterRequest;
import org.lab.roomboo.core.service.AlertService;
import org.lab.roomboo.core.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.integration.handler.LoggingHandler.Level;
import org.springframework.messaging.MessageChannel;
import org.springframework.retry.support.RetrySimulation.SleepSequence;

@Configuration
public class UserCreationFlow {

	@Autowired
	private AppUserService appUserService;

	@Autowired
	private AlertService alertService;

	@Bean(name = "channel-sign-up-input")
	MessageChannel signUpChannelIn() {
		return MessageChannels.direct().get();
	}

	@Bean(name = "channel-sign-up-output")
	MessageChannel signUpChannelOut() {
		return MessageChannels.direct().get();
	}

	@Bean(name = "channel-sign-up-error")
	MessageChannel signUpChannelError() {
		return MessageChannels.direct().get();
	}

	@Bean(name = "channel-notification")
	MessageChannel userCreatedChannel() {
		return MessageChannels.publishSubscribe("channel-notification-test").get();
	}

	//@formatter:off
	@Bean
	IntegrationFlow userSignUpFlow() {
		
		return IntegrationFlows
			.from(signUpChannelIn())
			.log(Level.INFO, "Processing user sign-up")
			.handle(AppUserRegisterRequest.class, (request, headers) -> appUserService.register(request))
			.log(Level.INFO, "Processed user sign-up")
			.publishSubscribeChannel(c -> c.applySequence(false)
				.subscribe(f -> f
					.channel(userCreatedChannel())))
			.channel(signUpChannelOut())
			.get();
	}
	//@formatter:on

	//@formatter:off
	@Bean
	IntegrationFlow notificationChannelFlow() {
		return IntegrationFlows
			.from("channel-notification")
			.log(Level.INFO, "Received message xxx")
			//.handle(alertService.create("test", "test"))
			.bridge()
			.get();
	}
	//@formatter:on
}

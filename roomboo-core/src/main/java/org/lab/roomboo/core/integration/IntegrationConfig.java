package org.lab.roomboo.core.integration;

import org.lab.roomboo.core.integration.RoombooIntegration.Channels;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.messaging.MessageChannel;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class IntegrationConfig {

	@Bean
	ThreadPoolTaskExecutor executor() {
		ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
		pool.setCorePoolSize(10);
		pool.setMaxPoolSize(10);
		pool.setWaitForTasksToCompleteOnShutdown(true);
		return pool;
	}

	@Bean(name = Channels.SignUpInput)
	MessageChannel channelSignUpInput() {
		return MessageChannels.direct(Channels.SignUpInput).get();
	}

	@Bean(name = Channels.SignUpOutput)
	MessageChannel channelSignUpOutput() {
		return MessageChannels.direct(Channels.SignUpOutput).get();
	}

	@Bean(name = Channels.SignUpError)
	MessageChannel channelSignUpError() {
		return MessageChannels.direct().get();
	}

	@Bean(name = Channels.SignUpConfirmationAuto)
	MessageChannel channel001() {
		return MessageChannels.direct(Channels.SignUpConfirmationAuto).get();
	}

	@Bean(name = Channels.AlertInput)
	MessageChannel channelSystemNotification() {
		return MessageChannels.publishSubscribe(executor()).get();
	}

	@Bean(name = Channels.SignUpConfirmationEmail)
	MessageChannel channel002() {
		return MessageChannels.publishSubscribe(executor()).get();
	}
}

package org.lab.roomboo.core.integration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.messaging.MessageChannel;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class IntegrationConfig {

	@Bean
	@ConditionalOnMissingBean
	ThreadPoolTaskExecutor executor() {
		ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
		pool.setCorePoolSize(10);
		pool.setMaxPoolSize(10);
		pool.setWaitForTasksToCompleteOnShutdown(true);
		return pool;
	}

	@Bean(name = RoomboChannels.SIGN_UP_IN)
	MessageChannel channelSignUpInput() {
		return MessageChannels.direct(RoomboChannels.SIGN_UP_IN).get();
	}

	@Bean(name = RoomboChannels.SIGN_UP_OUT)
	MessageChannel channelSignUpOutput() {
		return MessageChannels.direct(RoomboChannels.SIGN_UP_OUT).get();
	}

	@Bean(name = RoomboChannels.SIGN_UP_ERR)
	MessageChannel channelSignUpError() {
		return MessageChannels.direct().get();
	}

	@Bean(name = RoomboChannels.SIGN_UP_CONFIRMATION_AUTO)
	MessageChannel channel001() {
		return MessageChannels.direct(RoomboChannels.SIGN_UP_CONFIRMATION_AUTO).get();
	}

	@Bean(name = RoomboChannels.RESERVE_CONFIRMATION_AUTO)
	MessageChannel channelReserveConfirmationAuto() {
		return MessageChannels.direct().get();
	}

	@Bean(name = RoomboChannels.RESERVE_CONFIRMATION_EMAIL)
	MessageChannel channelReserveConfirmationEmail() {
		return MessageChannels.direct().get();
	}

	@Bean(name = RoomboChannels.ALERT_IN)
	MessageChannel channelSystemNotification() {
		return MessageChannels.publishSubscribe(executor()).get();
	}

	@Bean(name = RoomboChannels.SIGN_UP_CONFIRMATION_EMAIL)
	MessageChannel channel002() {
		return MessageChannels.publishSubscribe(executor()).get();
	}
}

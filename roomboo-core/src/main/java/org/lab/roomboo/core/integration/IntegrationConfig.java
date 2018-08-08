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

	@Bean(name = Channels.AlertInput)
	MessageChannel channelSystemNotification() {
		return MessageChannels.publishSubscribe(executor()).get();
	}
}

package org.lab.roomboo.core.integration.flow;

import org.apache.commons.lang3.StringUtils;
import org.lab.roomboo.core.integration.handler.TokenCleanerHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.messaging.support.GenericMessage;

@Configuration
public class SchedulerFlowConfig {

	@Autowired
	private TokenCleanerHandler tokenCleanerHandler;

	// Every day at 3 AM
	@Value("${app.env.scheduler.cron.token-cleanup:0 0 3 * * *}")
	private String cronTokenCleaner;

	@Bean
	IntegrationFlow flowTokenCleaner() { //@formatter:off
		return IntegrationFlows
			.from(() -> new GenericMessage<>(StringUtils.EMPTY), e -> e.poller(p -> p.cron(cronTokenCleaner)))
			.handle(tokenCleanerHandler)
			.get();
	} //@formatter:on

}

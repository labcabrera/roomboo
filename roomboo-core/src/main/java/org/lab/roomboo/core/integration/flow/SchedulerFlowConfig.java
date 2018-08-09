package org.lab.roomboo.core.integration.flow;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.messaging.support.GenericMessage;

@Configuration
public class SchedulerFlowConfig {

	@Value("${todo:0/15 * * * * *}")
	private String cronTokenCleaner;

	@Bean
	IntegrationFlow userSignUpFlow() { // formatter:off
		return IntegrationFlows
			.from(() -> new GenericMessage<>(StringUtils.EMPTY + "_test"), e -> e.poller(p -> p.cron(cronTokenCleaner)))
			.handle(System.out::println).get();
	} //@formatter:on

}

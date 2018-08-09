package org.lab.roomboo.core.integration.flow;

import org.lab.roomboo.core.integration.Channels;
import org.lab.roomboo.core.integration.handler.PayloadValidatorHandler;
import org.lab.roomboo.core.integration.handler.ReserveTokenConfirmationHandler;
import org.lab.roomboo.core.integration.handler.ReserveTokenGeneratorHandler;
import org.lab.roomboo.core.integration.router.ReserveConfirmationRouter;
import org.lab.roomboo.core.integration.transformer.BookingRequestTransformer;
import org.lab.roomboo.core.integration.transformer.ReserveConfirmationEmailTransformer;
import org.lab.roomboo.domain.model.Reserve;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.handler.LoggingHandler.Level;

@Configuration
public class ReserveFlowConfig {

	@Autowired
	private PayloadValidatorHandler payloadValidator;

	@Autowired
	private BookingRequestTransformer bookingRequestTransformer;

	@Autowired
	private ReserveConfirmationRouter reserveConfirmationRouter;

	@Autowired
	private ReserveTokenGeneratorHandler tokenConfirmationGeneratorHandler;

	@Autowired
	private ReserveTokenConfirmationHandler tokenConfirmationHandler;

	@Autowired
	private ReserveConfirmationEmailTransformer emailTransformer;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Bean
	IntegrationFlow flowBookingRequest() { //@formatter:off
		return IntegrationFlows
			.from(Channels.BookingInput)
			.log(Level.INFO, SignUpFlowConfig.class.getName(), m -> "Received sign-up message: " + m.getPayload())
			.handle(payloadValidator)
			.transform(bookingRequestTransformer)
			.handle(Reserve.class, (request, headers) -> {
				mongoTemplate.save(request);
				return request;
			})
			.publishSubscribeChannel(c -> c.applySequence(false)
				.subscribe(f -> f
					.route(reserveConfirmationRouter)))
//				.subscribe(f -> f
//					.transform(alertSignUpTransformer)
//					.channel(Channels.AlertInput)))
			.channel(Channels.BookingOutput)
			.get();
	} //@formatter:on

	@Bean
	IntegrationFlow flowReserveConfirmationEmail() { //@formatter:off
		return IntegrationFlows
			.from(Channels.ReserveConfirmationEmail)
			.log(Level.INFO, ReserveFlowConfig.class.getName(), m -> "Received email reserve confirmation message: " + m.getPayload())
			.handle(tokenConfirmationGeneratorHandler)
			.transform(emailTransformer)
			.channel(Channels.EmailOutput)
			.get();
	} //@formatter:on

	@Bean
	IntegrationFlow flowBookingConfirmation() { //@formatter:off
		return IntegrationFlows
			.from(Channels.BookingTokenConfirmationInput)
			.log(Level.INFO, ReserveFlowConfig.class.getName(), m -> "Received reserve token confirmation message: " + m.getPayload())
			.handle(tokenConfirmationHandler)
			.channel(Channels.BookingTokenConfirmationOutput)
			.get();
	} //@formatter:on

	@Bean
	IntegrationFlow flowBookingCancelation() { //@formatter:off
		return IntegrationFlows
			.from(Channels.BookingTokenCancelationInput)
			.channel(Channels.BookingTokenCancelationOutput)
			.get();
	} //@formatter:on

}

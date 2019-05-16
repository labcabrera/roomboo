package org.lab.roomboo.core.integration.flow;

import org.lab.roomboo.core.integration.RoomboChannels;
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
	IntegrationFlow flowBookingRequest() {
		return IntegrationFlows
			.from(RoomboChannels.BOOKING_IN)
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
			.channel(RoomboChannels.BOOKING_OUT)
			.get();
	}

	@Bean
	IntegrationFlow flowReserveConfirmationEmail() {
		return IntegrationFlows
			.from(RoomboChannels.RESERVE_CONFIRMATION_EMAIL)
			.log(Level.INFO, ReserveFlowConfig.class.getName(), m -> "Received email reserve confirmation message: " + m.getPayload())
			.handle(tokenConfirmationGeneratorHandler)
			.transform(emailTransformer)
			.channel(RoomboChannels.EMAIL_OUT)
			.get();
	}

	@Bean
	IntegrationFlow flowBookingConfirmation() {
		return IntegrationFlows
			.from(RoomboChannels.BOOKING_TOKEN_CONFIRMATION_IN)
			.log(Level.INFO, ReserveFlowConfig.class.getName(), m -> "Received reserve token confirmation message: " + m.getPayload())
			.handle(tokenConfirmationHandler)
			.channel(RoomboChannels.BOOKING_TOKEN_CONFIRMATION_OUT)
			.get();
	}

	@Bean
	IntegrationFlow flowBookingCancelation() {
		return IntegrationFlows
			.from(RoomboChannels.BOOKING_TOKEN_CANCELLATION_IN)
			.channel(RoomboChannels.BOOKING_TOKEN_CANCELLATION_OUT)
			.get();
	}

}

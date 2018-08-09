package org.lab.roomboo.core.integration.gateway;

import org.lab.roomboo.core.integration.Channels;
import org.lab.roomboo.core.model.BookingRequest;
import org.lab.roomboo.domain.model.Reserve;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway(name = "gateway-booking")
public interface BookingGateway {

	@Gateway(requestChannel = Channels.BookingInput, replyChannel = Channels.BookingOutput)
	Reserve processBookingRequest(BookingRequest request);

	@Gateway(requestChannel = Channels.BookingTokenConfirmationInput,
		replyChannel = Channels.BookingTokenConfirmationOutput)
	Reserve processReserveConfirmationByToken(String token);

	Reserve processCodeReserveConfirmationByCode(String reserveId, String code);

	Reserve processReserveCancelationByToken(String token);

}

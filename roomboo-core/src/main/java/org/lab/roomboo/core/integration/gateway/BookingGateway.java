package org.lab.roomboo.core.integration.gateway;

import org.lab.roomboo.core.integration.RoomboChannels;
import org.lab.roomboo.core.model.BookingRequest;
import org.lab.roomboo.domain.model.Reserve;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway(name = "gateway-booking")
public interface BookingGateway {

	@Gateway(requestChannel = RoomboChannels.BOOKING_IN, replyChannel = RoomboChannels.BOOKING_OUT)
	Reserve processBookingRequest(BookingRequest request);

	@Gateway(requestChannel = RoomboChannels.BOOKING_TOKEN_CONFIRMATION_IN, replyChannel = RoomboChannels.BOOKING_TOKEN_CONFIRMATION_OUT)
	Reserve processReserveConfirmationByToken(String token);

	Reserve processCodeReserveConfirmationByCode(String reserveId, String code);

	Reserve processReserveCancelationByToken(String token);

}

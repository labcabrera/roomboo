package org.lab.roomboo.core.integration.gateway;

import org.lab.roomboo.core.integration.RoomboChannels;
import org.lab.roomboo.core.integration.handler.PayloadValidatorHandler;
import org.lab.roomboo.core.model.SignUpRequest;
import org.lab.roomboo.domain.model.AppUser;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.GatewayHeader;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway(name = "gateway-sign-up")
public interface SignUpGateway {

	@Gateway(requestChannel = RoomboChannels.SIGN_UP_IN, replyChannel = RoomboChannels.SIGN_UP_OUT)
	AppUser signUp(SignUpRequest request);

	@Gateway(requestChannel = RoomboChannels.SIGN_UP_IN, replyChannel = RoomboChannels.SIGN_UP_OUT, headers = {
		@GatewayHeader(name = PayloadValidatorHandler.HEADER_PRE_VALIDATED, value = "true") })
	AppUser signUpPrevalidated(SignUpRequest request);

	@Gateway(requestChannel = RoomboChannels.USER_NEW_TOKEN_CONFIRMATION_IN, replyChannel = RoomboChannels.USER_NEW_TOKEN_CONFIRMATION_OUT)
	AppUser confirmationTokenRequest(String userId);

	@Gateway(requestChannel = RoomboChannels.USER_TOKEN_CONFIRMATION_IN, replyChannel = RoomboChannels.USER_TOKEN_CONFIRMATION_OUT)
	AppUser tokenConfirmation(String token);

}
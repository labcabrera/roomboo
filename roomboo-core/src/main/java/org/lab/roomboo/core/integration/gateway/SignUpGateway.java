package org.lab.roomboo.core.integration.gateway;

import org.lab.roomboo.core.integration.Channels;
import org.lab.roomboo.core.integration.transformer.PayloadValidatorHandler;
import org.lab.roomboo.core.model.SignUpRequest;
import org.lab.roomboo.domain.model.AppUser;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.GatewayHeader;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway(name = "gateway-sign-up")
public interface SignUpGateway {

	@Gateway(requestChannel = Channels.SignUpInput, replyChannel = Channels.SignUpOutput)
	AppUser signUp(SignUpRequest request);

	@Gateway(requestChannel = Channels.SignUpInput,
		replyChannel = Channels.SignUpOutput,
		headers = { @GatewayHeader(name = PayloadValidatorHandler.HEADER_PRE_VALIDATED, value = "true") })
	AppUser signUpPrevalidated(SignUpRequest request);

	@Gateway(requestChannel = Channels.UserNewTokenConfirmationInput,
		replyChannel = Channels.UserNewTokenConfirmationOutput)
	AppUser confirmationTokenRequest(String userId);

	@Gateway(requestChannel = Channels.UserTokenConfirmationInput, replyChannel = Channels.UserTokenConfirmationOutput)
	AppUser tokenConfirmation(String token);

}
package org.lab.roomboo.core.integration.gateway;

import org.lab.roomboo.core.integration.RoombooIntegration.Channels;
import org.lab.roomboo.core.model.SignUpRequest;
import org.lab.roomboo.domain.model.AppUser;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway(name = "gateway-sign-up")
public interface SignUpGateway {

	@Gateway(requestChannel = Channels.SignUpInput, replyChannel = Channels.SignUpOutput)
	AppUser signUp(SignUpRequest request);

	@Gateway(requestChannel = Channels.UserTokenConfirmationInput, replyChannel = Channels.UserTokenConfirmationOutput)
	AppUser tokenConfirmation(String token);

}
package org.lab.roomboo.core.integration.gateway;

import org.lab.roomboo.core.integration.RoombooIntegration.Channels;
import org.lab.roomboo.core.model.SignUpRequest;
import org.lab.roomboo.domain.model.AppUser;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway(name = "gateway-sign-up",
	defaultRequestChannel = Channels.SignUpInput,
	defaultReplyChannel = Channels.SignUpOutput,
	errorChannel = Channels.SignUpError)
public interface SignUpGateway {

	@Gateway
	AppUser signUp(SignUpRequest request);

}
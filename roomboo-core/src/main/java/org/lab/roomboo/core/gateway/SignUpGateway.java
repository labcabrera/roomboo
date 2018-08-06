package org.lab.roomboo.core.gateway;

import org.lab.roomboo.core.model.AppUserRegisterRequest;
import org.lab.roomboo.domain.model.AppUser;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway(name = "gateway-sign-up",
	defaultRequestChannel = "channel-sign-up-input",
	defaultReplyChannel = "channel-sign-up-output",
	errorChannel = "channel-sign-up-error")
public interface SignUpGateway {

	@Gateway
	AppUser signUp(AppUserRegisterRequest request);

}
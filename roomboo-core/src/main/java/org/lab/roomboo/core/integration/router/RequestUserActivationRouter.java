package org.lab.roomboo.core.integration.router;

import java.util.Arrays;
import java.util.Collection;

import org.lab.roomboo.core.integration.RoombooIntegration.Channels;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.router.AbstractMessageRouter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

@Component
public class RequestUserActivationRouter extends AbstractMessageRouter {

	@Qualifier(Channels.SignUpConfirmationAuto)
	@Autowired
	private MessageChannel directConfirmationChannel;

	@Qualifier(Channels.SignUpConfirmationEmail)
	@Autowired
	private MessageChannel emailConfirmationChannel;

	@Override
	protected Collection<MessageChannel> determineTargetChannels(Message<?> message) {
		// TODO check company
		return Arrays.asList(directConfirmationChannel);
	}

}

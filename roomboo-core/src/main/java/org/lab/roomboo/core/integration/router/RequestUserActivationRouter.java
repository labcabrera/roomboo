package org.lab.roomboo.core.integration.router;

import java.util.Collection;

import org.springframework.integration.router.AbstractMessageRouter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

public class RequestUserActivationRouter extends AbstractMessageRouter {

	@Override
	protected Collection<MessageChannel> determineTargetChannels(Message<?> message) {
		// TODO Auto-generated method stub
		return null;
	}

}

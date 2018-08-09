package org.lab.roomboo.core.integration.router;

import java.util.Arrays;
import java.util.Collection;

import org.lab.roomboo.core.integration.Channels;
import org.lab.roomboo.domain.model.Reserve;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.router.AbstractMessageRouter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

@Component
// @Slf4j
public class ReserveConfirmationRouter extends AbstractMessageRouter {

	@Qualifier(Channels.ReserveConfirmationAuto)
	@Autowired
	private MessageChannel directConfirmationChannel;

	@Qualifier(Channels.ReserveConfirmationEmail)
	@Autowired
	private MessageChannel emailConfirmationChannel;

	// TODO
	@Override
	protected Collection<MessageChannel> determineTargetChannels(Message<?> message) {
		Reserve reserve = (Reserve) message.getPayload();
		return Arrays.asList(emailConfirmationChannel);
		//
		// Company company = companyRepository.findById(user.getCompany().getId()).get();
		// switch (company.getSignUpActivationMode()) {
		// case AUTO:
		// log.debug("Routing to auto-confirmation channel");
		// return Arrays.asList(directConfirmationChannel);
		// case EMAIL:
		// log.debug("Routing to email confirmation channel");
		// return Arrays.asList(emailConfirmationChannel);
		// default:
		// throw new RoombooException("");
		// }
	}

}

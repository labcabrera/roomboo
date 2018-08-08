package org.lab.roomboo.core.integration.router;

import java.util.Arrays;
import java.util.Collection;

import org.lab.roomboo.core.integration.RoombooIntegration.Channels;
import org.lab.roomboo.domain.exception.RoombooException;
import org.lab.roomboo.domain.model.AppUser;
import org.lab.roomboo.domain.model.Company;
import org.lab.roomboo.domain.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.router.AbstractMessageRouter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

@Component
public class UserActivationRouter extends AbstractMessageRouter {

	@Qualifier(Channels.SignUpConfirmationAuto)
	@Autowired
	private MessageChannel directConfirmationChannel;

	@Qualifier(Channels.SignUpConfirmationEmail)
	@Autowired
	private MessageChannel emailConfirmationChannel;

	@Autowired
	private CompanyRepository companyRepository;

	@Override
	protected Collection<MessageChannel> determineTargetChannels(Message<?> message) {
		AppUser user = (AppUser) message.getPayload();
		Company company = companyRepository.findById(user.getCompany().getId()).get();
		switch (company.getSignUpActivationMode()) {
		case AUTO:
			return Arrays.asList(directConfirmationChannel);
		case EMAIL:
			return Arrays.asList(emailConfirmationChannel);
		default:
			throw new RoombooException("");
		}
	}

}

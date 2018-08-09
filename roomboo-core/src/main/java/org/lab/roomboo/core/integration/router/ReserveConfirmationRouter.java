package org.lab.roomboo.core.integration.router;

import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.lang3.NotImplementedException;
import org.lab.roomboo.core.integration.Channels;
import org.lab.roomboo.domain.model.AppUser;
import org.lab.roomboo.domain.model.Company;
import org.lab.roomboo.domain.model.Reserve;
import org.lab.roomboo.domain.repository.AppUserRepository;
import org.lab.roomboo.domain.repository.CompanyRepository;
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
	private MessageChannel channelReserveConfirmationAuto;

	@Qualifier(Channels.ReserveConfirmationEmail)
	@Autowired
	private MessageChannel channelReserveConfirmationEmail;

	@Autowired
	private AppUserRepository userRepository;

	@Autowired
	private CompanyRepository companyRepository;

	@Override
	protected Collection<MessageChannel> determineTargetChannels(Message<?> message) {
		Reserve reserve = (Reserve) message.getPayload();
		AppUser user = userRepository.findById(reserve.getUser().getId()).get();
		Company company = companyRepository.findById(user.getCompany().getId()).get();
		
		//TODO check state user / room

		switch (company.getRegisterConfirmationMode()) {
		case EMAIL:
			return Arrays.asList(channelReserveConfirmationEmail);
		case AUTO:
			return Arrays.asList(channelReserveConfirmationAuto);
		default:
			throw new NotImplementedException("Register confirmation mode " + company.getRegisterConfirmationMode());
		}
	}

}

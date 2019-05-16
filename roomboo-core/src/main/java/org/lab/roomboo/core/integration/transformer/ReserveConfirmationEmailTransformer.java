package org.lab.roomboo.core.integration.transformer;

import java.util.Arrays;
import java.util.Locale;

import org.lab.roomboo.core.model.MailMessage;
import org.lab.roomboo.domain.exception.EntityNotFoundException;
import org.lab.roomboo.domain.model.AppUser;
import org.lab.roomboo.domain.model.Reserve;
import org.lab.roomboo.domain.model.ReserveConfirmationToken;
import org.lab.roomboo.domain.repository.AppUserRepository;
import org.lab.roomboo.domain.repository.ReserveConfirmationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.transformer.GenericTransformer;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
public class ReserveConfirmationEmailTransformer implements GenericTransformer<Reserve, MailMessage> {

	@Autowired
	private ReserveConfirmationTokenRepository repository;

	@Autowired
	private AppUserRepository userRepository;

	@Autowired
	private TemplateEngine templateEngine;

	@Override
	public MailMessage transform(Reserve reserve) {
		ReserveConfirmationToken token = repository.findByReserveId(reserve.getId())
			.orElseThrow(() -> new EntityNotFoundException("Missing confirmation token"));
		AppUser user = userRepository.findById(reserve.getUser().getId())
			.orElseThrow(() -> new EntityNotFoundException("Missing user token"));

		Context context = new Context(Locale.getDefault());
		context.setVariable("user", user);
		context.setVariable("reserve", reserve);
		context.setVariable("token", token);

		String htmlContent = templateEngine.process("mail-reserve-created", context);

		return MailMessage.builder()
			.subject("Reserve confirmation")
			.body(htmlContent)
			.recipients(Arrays.asList(user.getEmail()))
			.build();
	}

}

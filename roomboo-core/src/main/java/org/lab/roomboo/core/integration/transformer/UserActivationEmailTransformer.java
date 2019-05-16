package org.lab.roomboo.core.integration.transformer;

import java.util.Arrays;
import java.util.Locale;

import org.lab.roomboo.core.model.MailMessage;
import org.lab.roomboo.domain.exception.EntityNotFoundException;
import org.lab.roomboo.domain.model.AppUser;
import org.lab.roomboo.domain.model.UserConfirmationToken;
import org.lab.roomboo.domain.repository.UserConfirmationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.transformer.GenericTransformer;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
public class UserActivationEmailTransformer implements GenericTransformer<AppUser, MailMessage> {

	@Autowired
	private UserConfirmationTokenRepository repository;

	@Autowired
	private TemplateEngine templateEngine;

	@Override
	public MailMessage transform(AppUser source) {
		UserConfirmationToken token = repository.findByUserId(source.getId())
			.orElseThrow(() -> new EntityNotFoundException("Missing confirmation token"));
		Context context = new Context(Locale.getDefault());
		context.setVariable("user", source);
		context.setVariable("token", token);

		String htmlContent = templateEngine.process("mail-user-created", context);

		return MailMessage.builder()
			.subject("Roombo verification")
			.body(htmlContent)
			.recipients(Arrays.asList(source.getEmail()))
			.build();
	}

}

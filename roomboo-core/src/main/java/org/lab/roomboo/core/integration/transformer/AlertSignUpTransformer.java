package org.lab.roomboo.core.integration.transformer;

import java.time.LocalDateTime;

import org.lab.roomboo.domain.model.Alert;
import org.lab.roomboo.domain.model.Alert.AlertType;
import org.lab.roomboo.domain.model.AppUser;
import org.springframework.integration.transformer.GenericTransformer;
import org.springframework.stereotype.Component;

@Component
public class AlertSignUpTransformer implements GenericTransformer<AppUser, Alert> {

	private static final String TEMPLATE = "User: %s. Email: %s";

	@Override
	public Alert transform(AppUser source) {
		return Alert.builder() //@formatter:off
			.created(LocalDateTime.now())
			.alertType(AlertType.INFO)
			.subject("User registration")
			.message(String.format(TEMPLATE, source.getCompleteName(), source.getEmail()))
			.build(); //@formatter:on
	}

}
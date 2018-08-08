package org.lab.roomboo.core.integration.transformer;

import java.time.LocalDateTime;

import org.lab.roomboo.core.model.SignUpRequest;
import org.lab.roomboo.domain.exception.RoombooException;
import org.lab.roomboo.domain.model.AppUser;
import org.springframework.integration.transformer.GenericTransformer;
import org.springframework.stereotype.Component;

@Component
public class SignUpAppUserTransformer implements GenericTransformer<SignUpRequest, AppUser> {

	@Override
	public AppUser transform(SignUpRequest source) {

		// TODO testing error channel
		if (source.getEmail() == null) {
			throw new RoombooException("Null email");
		}

		return AppUser.builder() //@formatter:off
			.name(source.getName())
			.lastName(source.getLastName())
			.email(source.getEmail())
			.created(LocalDateTime.now())
			.build(); //@formatter:on
	}

}

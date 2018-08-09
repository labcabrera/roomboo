package org.lab.roomboo.core.integration.transformer;

import java.time.LocalDateTime;

import org.lab.roomboo.core.model.SignUpRequest;
import org.lab.roomboo.domain.model.AppUser;
import org.lab.roomboo.domain.model.Company;
import org.springframework.integration.transformer.GenericTransformer;
import org.springframework.stereotype.Component;

@Component
public class UserRegisterTransformer implements GenericTransformer<SignUpRequest, AppUser> {

	@Override
	public AppUser transform(SignUpRequest source) {
		return AppUser.builder() //@formatter:off
			.name(source.getName())
			.company(Company.builder().id(source.getCompanyId()).build())
			.lastName(source.getLastName())
			.email(source.getEmail())
			.created(LocalDateTime.now())
			.build(); //@formatter:on
	}

}

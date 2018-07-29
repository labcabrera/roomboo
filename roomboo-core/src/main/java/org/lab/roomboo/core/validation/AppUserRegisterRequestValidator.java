package org.lab.roomboo.core.validation;

import java.util.Optional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.lab.roomboo.core.model.AppUserRegisterRequest;
import org.lab.roomboo.domain.model.AppUser;
import org.lab.roomboo.domain.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class AppUserRegisterRequestValidator
	implements ConstraintValidator<ValidAppUserRegisterRequest, AppUserRegisterRequest> {

	@Autowired
	private AppUserRepository repository;

	@Override
	public boolean isValid(AppUserRegisterRequest value, ConstraintValidatorContext context) {
		boolean valid = true;
		Optional<AppUser> check = repository.findByEmail(value.getEmail());
		if (check.isPresent()) {
			context.buildConstraintViolationWithTemplate("validation.AppUserRegisterRequest.email.alreadyUsed")
				.addConstraintViolation();
			valid = false;
		}
		return valid;
	}

}

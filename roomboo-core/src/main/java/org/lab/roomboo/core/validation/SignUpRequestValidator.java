package org.lab.roomboo.core.validation;

import java.util.Optional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;
import org.lab.roomboo.core.model.SignUpRequest;
import org.lab.roomboo.domain.model.AppUser;
import org.lab.roomboo.domain.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class SignUpRequestValidator implements ConstraintValidator<ValidSignUpRequest, SignUpRequest> {

	@Autowired
	private AppUserRepository repository;

	@Override
	public boolean isValid(SignUpRequest value, ConstraintValidatorContext context) {
		boolean valid = true;
		if (StringUtils.isNotBlank(value.getEmail())) {
			Optional<AppUser> check = repository.findByEmail(value.getEmail(), value.getCompanyId());
			if (check.isPresent()) {
				context.buildConstraintViolationWithTemplate("validation.AppUserRegisterRequest.email.alreadyUsed")
					.addConstraintViolation();
				valid = false;
			}
		}
		return valid;
	}

}

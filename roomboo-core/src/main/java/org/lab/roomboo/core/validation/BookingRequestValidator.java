package org.lab.roomboo.core.validation;

import java.time.LocalDateTime;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;
import org.lab.roomboo.core.model.BookingRequest;
import org.lab.roomboo.domain.model.AppUser;
import org.lab.roomboo.domain.repository.AppUserRepository;
import org.lab.roomboo.domain.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class BookingRequestValidator implements ConstraintValidator<ValidBookingRequest, BookingRequest> {

	@Autowired
	private RoomRepository roomRepository;

	@Autowired
	private AppUserRepository ownerRepository;

	@Override
	public boolean isValid(BookingRequest value, ConstraintValidatorContext context) {
		boolean valid = true;
		valid &= validateRoom(value, context);
		valid &= validateAppUser(value, context);
		valid &= validateDates(value, context);
		if (StringUtils.isBlank(value.getName())) {
			context.buildConstraintViolationWithTemplate("Required reserve name").addConstraintViolation();
			valid = false;
		}
		return valid;
	}

	private boolean validateRoom(BookingRequest value, ConstraintValidatorContext context) {
		if (StringUtils.isBlank(value.getRoomId())) {
			context.buildConstraintViolationWithTemplate("Required roomId").addConstraintViolation();
			return false;
		}
		if (!roomRepository.findById(value.getRoomId()).isPresent()) {
			context.buildConstraintViolationWithTemplate("Invalid room identifier").addConstraintViolation();
			return false;
		}
		return true;
	}

	private boolean validateAppUser(BookingRequest value, ConstraintValidatorContext context) {
		if (StringUtils.isBlank(value.getUserId())) {
			context.buildConstraintViolationWithTemplate("Required user").addConstraintViolation();
			return false;
		}
		AppUser user = ownerRepository.findById(value.getUserId()).orElse(null);
		if (user == null) {
			context.buildConstraintViolationWithTemplate("Invalid user identifier").addConstraintViolation();
			return false;
		}
		LocalDateTime now = LocalDateTime.now();
		if (user.getActivation() == null || user.getActivation().isAfter(now)) {
			context.buildConstraintViolationWithTemplate("User is not active").addConstraintViolation();
			return false;
		}
		if (user.getLocked() != null && user.getLocked().isAfter(now)) {
			context.buildConstraintViolationWithTemplate("User is locked").addConstraintViolation();
			return false;
		}
		if (user.getExpiration() != null && user.getExpiration().isAfter(now)) {
			context.buildConstraintViolationWithTemplate("User is expired").addConstraintViolation();
			return false;
		}
		return true;
	}

	private boolean validateDates(BookingRequest value, ConstraintValidatorContext context) {
		boolean valid = true;
		if (value.getFrom() == null) {
			context.buildConstraintViolationWithTemplate("Required from").addConstraintViolation();
			valid = false;
		}
		else if (value.getFrom().isBefore(LocalDateTime.now())) {
			context.buildConstraintViolationWithTemplate("Start date must be after the current date")
				.addConstraintViolation();
			valid = false;
		}
		if (value.getTo() == null) {
			context.buildConstraintViolationWithTemplate("Required to").addConstraintViolation();
			valid = false;
		}
		else if (value.getTo().isBefore(LocalDateTime.now())) {
			context.buildConstraintViolationWithTemplate("End date must be after the current date")
				.addConstraintViolation();
			valid = false;
		}
		if (value.getFrom() != null && value.getTo() != null && value.getFrom().isAfter(value.getTo())) {
			context.buildConstraintViolationWithTemplate("Start date can not be after end date")
				.addConstraintViolation();
			valid = false;
		}
		return valid;
	}

}

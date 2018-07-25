package org.lab.roomboo.api.model.validation;

import java.time.LocalDateTime;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;
import org.lab.roomboo.api.model.BookingRequest;
import org.lab.roomboo.domain.repository.ReserveOwnerRepository;
import org.lab.roomboo.domain.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class BookingRequestValidator implements ConstraintValidator<ValidBookingRequest, BookingRequest> {

	@Autowired
	private RoomRepository roomRepository;

	@Autowired
	private ReserveOwnerRepository ownerRepository;

	@Override
	public boolean isValid(BookingRequest value, ConstraintValidatorContext context) {
		boolean valid = true;
		if (StringUtils.isBlank(value.getRoomId())) {
			context.buildConstraintViolationWithTemplate("Required roomId").addConstraintViolation();
			valid = false;
		}
		else if (!roomRepository.findById(value.getRoomId()).isPresent()) {
			context.buildConstraintViolationWithTemplate("Invalid roomId " + value.getRoomId())
				.addConstraintViolation();
			valid = false;
		}
		if (StringUtils.isBlank(value.getOwnerId())) {
			context.buildConstraintViolationWithTemplate("Required ownerId").addConstraintViolation();
			valid = false;
		}
		else if (!ownerRepository.findById(value.getOwnerId()).isPresent()) {
			context.buildConstraintViolationWithTemplate("Invalid ownerId " + value.getOwnerId())
				.addConstraintViolation();
			valid = false;
		}
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

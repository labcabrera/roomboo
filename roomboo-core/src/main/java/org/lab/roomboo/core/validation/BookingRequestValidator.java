package org.lab.roomboo.core.validation;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;
import org.lab.roomboo.core.model.BookingRequest;
import org.lab.roomboo.core.service.ReserveService;
import org.lab.roomboo.domain.model.AppUser;
import org.lab.roomboo.domain.model.Reserve;
import org.lab.roomboo.domain.model.Room;
import org.lab.roomboo.domain.repository.AppUserRepository;
import org.lab.roomboo.domain.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class BookingRequestValidator implements ConstraintValidator<ValidBookingRequest, BookingRequest> {

	@Autowired
	private RoomRepository roomRepository;

	@Autowired
	private AppUserRepository ownerRepository;

	@Autowired
	private ReserveService reserveService;

	@Value("${app.env.reserve.minuteMultiplier:15}")
	private Integer minuteMultiplier;

	@Override
	public boolean isValid(BookingRequest value, ConstraintValidatorContext context) {
		boolean valid = true;
		valid &= validateRoom(value, context);
		valid &= validateAppUser(value, context);
		valid &= validateDates(value, context);
		valid &= validateReseveDates(value, context);
		return valid;
	}

	private boolean validateRoom(BookingRequest value, ConstraintValidatorContext context) {
		if (StringUtils.isNotBlank(value.getRoomId())) {
			Room room = roomRepository.findById(value.getRoomId()).orElse(null);
			if (room == null) {
				context.buildConstraintViolationWithTemplate("Invalid room identifier").addConstraintViolation();
				return false;
			}
			if (room.getLocked() != null && room.getLocked().isBefore(LocalDateTime.now())) {
				context.buildConstraintViolationWithTemplate("Room locked").addConstraintViolation();
				return false;
			}
		}
		return true;
	}

	private boolean validateAppUser(BookingRequest value, ConstraintValidatorContext context) {
		if (StringUtils.isNotBlank(value.getUserId())) {
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
			if (user.getLocked() != null && user.getLocked().isBefore(now)) {
				context.buildConstraintViolationWithTemplate("User is locked").addConstraintViolation();
				return false;
			}
			if (user.getExpiration() != null && user.getExpiration().isBefore(now)) {
				context.buildConstraintViolationWithTemplate("User is expired").addConstraintViolation();
				return false;
			}
		}
		return true;
	}

	private boolean validateDates(BookingRequest value, ConstraintValidatorContext context) {
		boolean valid = true;
		final LocalDateTime from = value.getFrom();
		final LocalDateTime to = value.getTo();
		if (from != null && !isValidDate(from)) {
			context.buildConstraintViolationWithTemplate("Invalid start date").addConstraintViolation();
			valid = false;
		}
		if (to != null && !isValidDate(to)) {
			context.buildConstraintViolationWithTemplate("Invalid end date").addConstraintViolation();
			valid = false;
		}
		if (from != null && to != null && from.isAfter(to)) {
			context.buildConstraintViolationWithTemplate("Start date can not be after end date")
				.addConstraintViolation();
			valid = false;
		}
		return valid;
	}

	private boolean validateReseveDates(BookingRequest value, ConstraintValidatorContext context) {
		if (value.getFrom() != null || value.getTo() != null && StringUtils.isNotEmpty(value.getRoomId())) {
			Optional<Reserve> optional = reserveService.findInRange(value.getRoomId(), value.getFrom(), value.getTo());
			if (optional.isPresent()) {
				context.buildConstraintViolationWithTemplate("Room is not available at that time")
					.addConstraintViolation();
				return false;
			}
		}
		return true;
	}

	private boolean isValidDate(LocalDateTime dateTime) {
		boolean check01 = dateTime.getMinute() % minuteMultiplier == 0;
		boolean check02 = dateTime.getSecond() == 0;
		boolean check03 = dateTime.getNano() == 0;
		return check01 && check02 && check03;
	}

}

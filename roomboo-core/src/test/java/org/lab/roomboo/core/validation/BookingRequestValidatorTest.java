package org.lab.roomboo.core.validation;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lab.roomboo.core.model.BookingRequest;
import org.lab.roomboo.core.service.ReserveService;
import org.lab.roomboo.domain.model.AppUser;
import org.lab.roomboo.domain.model.Room;
import org.lab.roomboo.domain.repository.AppUserRepository;
import org.lab.roomboo.domain.repository.RoomRepository;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@RunWith(SpringRunner.class)
@ContextConfiguration
public class BookingRequestValidatorTest {

	@Autowired
	private Validator validator;

	@Test
	public void validateOk01() {
		BookingRequest request = buildEntity();
		Set<ConstraintViolation<BookingRequest>> result = validator.validate(request);
		Assert.assertTrue(result.isEmpty());
	}

	@Test
	public void validateOk02() {
		BookingRequest request = buildEntity();
		request.setFrom(LocalDateTime.parse("2020-01-01T10:15:00.000"));
		request.setFrom(LocalDateTime.parse("2020-01-01T10:45:00.000"));
		Set<ConstraintViolation<BookingRequest>> result = validator.validate(request);
		Assert.assertTrue(result.isEmpty());
	}

	@Test
	public void validateRoomLocked() {
		BookingRequest request = buildEntity();
		request.setRoomId("roomIdLocked");
		Set<ConstraintViolation<BookingRequest>> result = validator.validate(request);
		Assert.assertFalse(result.isEmpty());
	}

	@Test
	public void validateUserNotActive() {
		BookingRequest request = buildEntity();
		request.setUserId("userNotActive");
		Set<ConstraintViolation<BookingRequest>> result = validator.validate(request);
		Assert.assertFalse(result.isEmpty());
	}

	@Test
	public void validateUserLocked() {
		BookingRequest request = buildEntity();
		request.setUserId("userLocked");
		Set<ConstraintViolation<BookingRequest>> result = validator.validate(request);
		Assert.assertFalse(result.isEmpty());
	}

	@Test
	public void validateUserExpired() {
		BookingRequest request = buildEntity();
		request.setUserId("userExpired");
		Set<ConstraintViolation<BookingRequest>> result = validator.validate(request);
		Assert.assertFalse(result.isEmpty());
	}

	@Test
	public void validateDateInvalidSecond() {
		BookingRequest request = buildEntity();
		request.setFrom(LocalDateTime.parse("2020-01-01T10:00:15.000"));
		Set<ConstraintViolation<BookingRequest>> result = validator.validate(request);
		Assert.assertFalse(result.isEmpty());
	}

	@Test
	public void validateDateInvalidNano() {
		BookingRequest request = buildEntity();
		request.setFrom(LocalDateTime.parse("2020-01-01T10:00:00.015"));
		Set<ConstraintViolation<BookingRequest>> result = validator.validate(request);
		Assert.assertFalse(result.isEmpty());
	}

	@Test
	public void validateNullName() {
		BookingRequest request = buildEntity();
		request.setName(null);
		Set<ConstraintViolation<BookingRequest>> result = validator.validate(request);
		Assert.assertFalse(result.isEmpty());
	}

	@Test
	public void validateEmptyName() {
		BookingRequest request = buildEntity();
		request.setName(StringUtils.EMPTY);
		Set<ConstraintViolation<BookingRequest>> result = validator.validate(request);
		Assert.assertFalse(result.isEmpty());
	}

	@Test
	public void validateNullFrom() {
		BookingRequest request = buildEntity();
		request.setFrom(null);
		Set<ConstraintViolation<BookingRequest>> result = validator.validate(request);
		Assert.assertFalse(result.isEmpty());
	}

	@Test
	public void validateNullTo() {
		BookingRequest request = buildEntity();
		request.setTo(null);
		Set<ConstraintViolation<BookingRequest>> result = validator.validate(request);
		Assert.assertFalse(result.isEmpty());
	}

	private BookingRequest buildEntity() {
		return BookingRequest.builder()
			.name("Reserve name")
			.roomId("roomIdOk")
			.userId("userIdOk")
			.from(LocalDateTime.parse("2020-01-01T10:00:00.000"))
			.to(LocalDateTime.parse("2020-01-01T11:00:00.000"))
			.build();
	}

	@Configuration
	public static class BookingRequestValidatorTestConfig {

		@Bean
		Validator validator() {
			return new LocalValidatorFactoryBean();
		}

		@Bean
		RoomRepository roomRepository() {
			RoomRepository mock = Mockito.mock(RoomRepository.class);
			Mockito.when(mock.findById("roomIdOk")).thenReturn(Optional.of(Room.builder().id("roomIdOk").build()));
			Mockito.when(mock.findById("roomIdLocked"))
				.thenReturn(Optional.of(Room.builder().id("roomIdLocked").locked(LocalDateTime.parse("2010-01-01T00:00:00.000")).build()));
			return mock;
		}

		@Bean
		AppUserRepository appUserRepository() {
			AppUserRepository mock = Mockito.mock(AppUserRepository.class);

			AppUser userActive = AppUser.builder()
				.id("userIdOk")
				.email("test@roomboo.org")
				.name("Test")
				.activation(LocalDateTime.parse("2010-01-01T00:00:00.000"))
				.build();
			AppUser userInactive = AppUser.builder()
				.id("userNotActive")
				.name("Inactive")
				.email("inactive@roomboo.org")
				.build();
			AppUser userLocked = AppUser.builder()
				.id("userLoked")
				.name("Locked")
				.email("locked@roomboo.org")
				.activation(LocalDateTime.parse("2010-01-01T00:00:00.000"))
				.locked(LocalDateTime.parse("2010-02-01T00:00:00.000"))
				.build();
			AppUser userExpired = AppUser.builder()
				.id("userExpired")
				.name("Expired")
				.email("expired@roomboo.org")
				.activation(LocalDateTime.parse("2010-01-01T00:00:00.000"))
				.expiration(LocalDateTime.parse("2010-02-01T00:00:00.000"))
				.build();

			Mockito.when(mock.findById("userIdOk")).thenReturn(Optional.of(userActive));
			Mockito.when(mock.findById("userNotActive")).thenReturn(Optional.of(userInactive));
			Mockito.when(mock.findById("userLocked")).thenReturn(Optional.of(userLocked));
			Mockito.when(mock.findById("userExpired")).thenReturn(Optional.of(userExpired));
			return mock;
		}

		@Bean
		@ConditionalOnMissingBean
		ReserveService reserveService() {
			ReserveService mock = Mockito.mock(ReserveService.class);
			return mock;
		}

		@Bean
		BookingRequestValidator bookingRequestValidator() {
			return new BookingRequestValidator();
		}

		@Bean
		MongoTemplate mongoTemplate() {
			return Mockito.mock(MongoTemplate.class);
		}

	}

}

package org.lab.roomboo.core.model;

import java.time.LocalDateTime;

import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.lab.roomboo.core.validation.ValidBookingRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ValidBookingRequest
@Valid
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingRequest {

	@NotBlank(message = "validation.BookingRequest.roomId.required")
	private String roomId;

	@NotBlank(message = "validation.BookingRequest.userId.required")
	private String userId;

	@NotBlank(message = "validation.BookingRequest.name.required")
	private String name;

	@NotNull(message = "validation.BookingRequest.from.required")
	@FutureOrPresent(message = "validation.BookingRequest.from.requiredFuture")
	private LocalDateTime from;

	@NotNull(message = "validation.BookingRequest.to.required")
	@Future(message = "validation.BookingRequest.to.requiredFuture")
	private LocalDateTime to;

}

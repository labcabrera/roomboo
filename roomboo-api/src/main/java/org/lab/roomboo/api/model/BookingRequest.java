package org.lab.roomboo.api.model;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import org.lab.roomboo.api.model.validation.ValidBookingRequest;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ValidBookingRequest
public class BookingRequest {

	@NotNull
	private String roomId;

	@NotNull
	private String ownerId;

	@NotNull
	private String name;

	@NotNull
	private LocalDateTime from;

	@NotNull
	private LocalDateTime to;

}

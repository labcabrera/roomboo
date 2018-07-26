package org.lab.roomboo.core.model;

import java.time.LocalDateTime;

import org.lab.roomboo.core.validation.ValidBookingRequest;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ValidBookingRequest
public class BookingRequest {

	private String roomId;

	private String userId;

	private String name;

	private LocalDateTime from;

	private LocalDateTime to;

}

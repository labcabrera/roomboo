package org.lab.roomboo.core.model;

import java.time.LocalDateTime;

import org.lab.roomboo.core.validation.ValidBookingRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ValidBookingRequest
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingRequest {

	private String roomId;

	private String userId;

	private String name;

	private LocalDateTime from;

	private LocalDateTime to;

}

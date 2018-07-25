package org.lab.roomboo.api.model;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BookingRequest {

	private String roomId;

	private String ownerId;

	private LocalDateTime from;

	private LocalDateTime to;

}

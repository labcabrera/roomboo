package org.lab.roomboo.api.model;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RoomReserveRequest {

	@NotNull
	private String ownerId;

	@NotNull
	private String roomId;

	private LocalDateTime from;

	private LocalDateTime to;
}

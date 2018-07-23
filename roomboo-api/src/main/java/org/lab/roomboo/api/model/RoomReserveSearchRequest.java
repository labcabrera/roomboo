package org.lab.roomboo.api.model;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RoomReserveSearchRequest {

	private String ownerId;

	private String roomId;

	private LocalDate date;

}

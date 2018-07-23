package org.lab.roomboo.api.model;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RoomCalendarEvent {

	private LocalDate from;

	private LocalDate to;

}

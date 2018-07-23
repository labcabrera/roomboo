package org.lab.roomboo.api.controller;

import java.time.LocalDate;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/v1/calendars")
@RestController
public class RoomCalendarService {

	@GetMapping
	public Object find(//@formatter:off
			@RequestParam(value = "roomId", required = true) String roomId,
			@RequestParam(value = "date", required = true) LocalDate date) { //@formatter:on
		return null;
	}

}

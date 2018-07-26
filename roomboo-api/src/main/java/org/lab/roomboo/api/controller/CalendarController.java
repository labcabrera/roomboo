package org.lab.roomboo.api.controller;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.fromController;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.lab.roomboo.api.config.SwaggerConfig;
import org.lab.roomboo.api.resources.ReserveResource;
import org.lab.roomboo.core.service.ReserveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

@RestController
@RequestMapping(value = "/v1/calendars", produces = "application/hal+json")
public class CalendarController {

	@Autowired
	private ReserveService reserveService;

	@ApiOperation(value = "Room calenday today",
		authorizations = { @Authorization(value = SwaggerConfig.API_KEY_NAME) })
	@GetMapping("/{roomId}/today")
	public ResponseEntity<Resources<ReserveResource>> findToday(@PathVariable String roomId) {
		return find(roomId, LocalDate.now());
	}

	@ApiOperation(value = "Room calendar tomorrow",
		authorizations = { @Authorization(value = SwaggerConfig.API_KEY_NAME) })
	@GetMapping("/{roomId}/tomorrow")
	public ResponseEntity<Resources<ReserveResource>> findTomorrow(@PathVariable String roomId) {
		return find(roomId, LocalDate.now().plusDays(1));
	}

	private ResponseEntity<Resources<ReserveResource>> find(String roomId, LocalDate date) {
		List<ReserveResource> collection = reserveService.find(roomId, date).stream().map(ReserveResource::new)
			.collect(Collectors.toList());
		Resources<ReserveResource> resources = new Resources<>(collection);
		resources.add(new Link(ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString(), "self"));
		resources.add(new Link(fromController(ReserveController.class).build().toString(), "reserves"));
		resources.add(new Link(fromController(RoomController.class).build().toString(), "rooms"));
		resources.add(new Link(fromController(AppUserController.class).build().toString(), "owners"));
		return ResponseEntity.ok(resources);
	}

}

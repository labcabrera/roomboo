package org.lab.roomboo.api.controller;

import java.net.URI;

import org.lab.roomboo.api.config.SwaggerConfig;
import org.lab.roomboo.api.model.BookingRequest;
import org.lab.roomboo.api.resources.ReserveResource;
import org.lab.roomboo.api.service.BookingService;
import org.lab.roomboo.domain.model.Reserve;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

@RestController
@RequestMapping(value = "/v1/booking", produces = "application/hal+json")
public class BookingController {

	@Autowired
	private BookingService bookingService;

	@ApiOperation(value = "Reserve owner insert",
		authorizations = { @Authorization(value = SwaggerConfig.API_KEY_NAME) })
	@PostMapping
	public ResponseEntity<ReserveResource> processRequest(@RequestBody BookingRequest request) {
		Reserve reserve = bookingService.processBookingRequest(request);
		URI uri = MvcUriComponentsBuilder.fromController(ReserveController.class).path("/{id}")
			.buildAndExpand(reserve.getId()).toUri();
		return ResponseEntity.created(uri).body(new ReserveResource(reserve));
	}

}

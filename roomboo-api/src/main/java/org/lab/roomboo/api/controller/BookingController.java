package org.lab.roomboo.api.controller;

import java.io.IOException;
import java.net.URI;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.NotImplementedException;
import org.lab.roomboo.api.config.SwaggerConfig;
import org.lab.roomboo.api.resource.ReserveResource;
import org.lab.roomboo.core.integration.gateway.BookingGateway;
import org.lab.roomboo.core.model.BookingRequest;
import org.lab.roomboo.domain.exception.ReserveConfirmationException;
import org.lab.roomboo.domain.model.Reserve;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/v1/booking", produces = "application/hal+json")
@Slf4j
public class BookingController {

	@Autowired
	private BookingGateway bookingGateway;

	@Value("${app.env.token.reserve.redirect-uri:}")
	private String confirmationRedirectUri;

	@Value("${app.env.token.reserve.cancelation-redirect-uri:}")
	private String cancelationRedirectUri;

	@ApiOperation(value = "Process booking request", authorizations = { @Authorization(value = SwaggerConfig.API_KEY_NAME) })
	@PostMapping
	public ResponseEntity<ReserveResource> processRequest(@RequestBody @Valid BookingRequest request) {
		Reserve reserve = bookingGateway.processBookingRequest(request);
		URI uri = MvcUriComponentsBuilder.fromController(ReserveController.class).path("/{id}")
			.buildAndExpand(reserve.getId()).toUri();
		return ResponseEntity.created(uri).body(new ReserveResource(reserve));
	}

	@ApiOperation(value = "Code confirmation", authorizations = { @Authorization(value = SwaggerConfig.API_KEY_NAME) })
	@PostMapping("accept/{reserveId}/{code}")
	public ResponseEntity<ReserveResource> processCodeConfirmation(@PathVariable("reserveId") String reserveId,
		@PathVariable("code") String code) {
		// TODO
		throw new NotImplementedException("Not implemented");
	}

	@ApiOperation(value = "Token confirmation")
	@GetMapping("/accept/{token}")
	public void processTokenConfirmation(@PathVariable String token, HttpServletResponse response) {
		try {
			Reserve reserve = bookingGateway.processReserveConfirmationByToken(token);
			String redirectUri = buildRedirectUri(reserve, confirmationRedirectUri);
			log.debug("Confirmation redirect: {}", redirectUri);
			response.sendRedirect(redirectUri);
		}
		catch (ReserveConfirmationException ex) {
			log.warn("Invalid token confirmation: " + ex.getMessage());
			log.trace("Confirmation error", ex);
		}
		catch (IOException ex) {
			log.error("Confirmation redirection error", ex);
		}
	}

	@ApiOperation(value = "Token cancelation")
	@GetMapping("/decline/{token}")
	public void processTokenCancelation(@PathVariable String token, HttpServletResponse response) {
		try {
			Reserve reserve = bookingGateway.processReserveCancelationByToken(token);
			String redirectUri = buildRedirectUri(reserve, cancelationRedirectUri);
			log.debug("Cancelation redirect: {}", redirectUri);
			response.sendRedirect(redirectUri);
		}
		catch (ReserveConfirmationException ex) {
			log.warn("Invalid token cancelation: " + ex.getMessage());
			log.trace("Confirmation cancelation", ex);
		}
		catch (IOException ex) {
			log.error("Cancelation redirection error", ex);
		}
	}

	private String buildRedirectUri(Reserve reserve, String baseUrl) {
		StringBuilder sb = new StringBuilder(baseUrl);
		if (!confirmationRedirectUri.contains("?")) {
			sb.append("?");
		}
		else {
			sb.append("&");
		}
		sb.append("reserveId=").append(reserve.getId());
		return sb.toString();
	}

}

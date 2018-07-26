package org.lab.roomboo.api.controller;

import java.io.IOException;
import java.net.URI;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.lab.roomboo.api.config.SwaggerConfig;
import org.lab.roomboo.api.resources.ReserveResource;
import org.lab.roomboo.core.model.BookingRequest;
import org.lab.roomboo.core.service.BookingService;
import org.lab.roomboo.domain.exception.TokenConfirmationException;
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
	private BookingService bookingService;

	@Value("${app.env.token.reserve.redirect-uri:}")
	private String confirmationRedirectUri;

	@ApiOperation(value = "Process booking request",
		authorizations = { @Authorization(value = SwaggerConfig.API_KEY_NAME) })
	@PostMapping
	public ResponseEntity<ReserveResource> processRequest(@RequestBody @Valid BookingRequest request) {
		Reserve reserve = bookingService.processBookingRequest(request);
		URI uri = MvcUriComponentsBuilder.fromController(ReserveController.class).path("/{id}")
			.buildAndExpand(reserve.getId()).toUri();
		return ResponseEntity.created(uri).body(new ReserveResource(reserve));
	}

	@ApiOperation(value = "Code confirmation", authorizations = { @Authorization(value = SwaggerConfig.API_KEY_NAME) })
	@PostMapping("confirmations/code/{code}")
	public ResponseEntity<?> processCodeConfirmation(@PathVariable String code) {
		return null;
	}

	@ApiOperation(value = "Token confirmation")
	@GetMapping("confirmations/token/{token}")
	public void processTokenConfirmation(@PathVariable String token, HttpServletResponse response) {
		try {
			Reserve reserve = bookingService.processTokenConfirmation(token);
			String redirectUri = buildRedirectUri(reserve);
			log.debug("Redirect: {}", redirectUri);
			response.sendRedirect(redirectUri);
		}
		catch (TokenConfirmationException ex) {
			log.warn("Invalid token confirmation: " + ex.getMessage());
			log.trace("Confirmation error", ex);
		}
		catch (IOException ex) {
			log.error("Redirection error", ex);
		}
	}

	private String buildRedirectUri(Reserve reserve) {
		StringBuilder sb = new StringBuilder(confirmationRedirectUri);
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

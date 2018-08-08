package org.lab.roomboo.api.controller;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.fromController;

import java.io.IOException;
import java.net.URI;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.lab.roomboo.api.config.SwaggerConfig;
import org.lab.roomboo.api.resource.AppUserResource;
import org.lab.roomboo.core.integration.gateway.SignUpGateway;
import org.lab.roomboo.core.model.SignUpRequest;
import org.lab.roomboo.core.service.SignUpService;
import org.lab.roomboo.domain.exception.UserConfirmationException;
import org.lab.roomboo.domain.model.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/v1/sign-up", produces = "application/hal+json")
@Slf4j
public class SignUpController {

	@Autowired
	private SignUpService signUpService;

	@Autowired
	private SignUpGateway signUpGateway;

	@Value("${app.env.token.user-register.redirect-uri:}")
	private String confirmationRedirectUri;

	@ApiOperation(value = "Register new app user",
		authorizations = { @Authorization(value = SwaggerConfig.API_KEY_NAME) })
	@PostMapping
	public ResponseEntity<AppUserResource> save(@Valid @RequestBody SignUpRequest request) {
		AppUser inserted = signUpGateway.signUp(request);
		URI uri = fromController(getClass()).path("/{id}").buildAndExpand(inserted.getId()).toUri();
		return ResponseEntity.created(uri).body(new AppUserResource(inserted));
	}

	@ApiOperation(value = "App user token confirmation",
		authorizations = { @Authorization(value = SwaggerConfig.API_KEY_NAME) })
	@GetMapping("/accept/{token}")
	public void confirmByToken(@PathVariable("token") String token, HttpServletResponse response) {
		try {
			AppUser user = signUpService.processConfirmationToken(token);
			String redirectUri = buildRedirectUri(user, confirmationRedirectUri);
			log.debug("Confirmation redirect: {}", redirectUri);
			response.sendRedirect(redirectUri);
		}
		catch (UserConfirmationException ex) {
			log.warn("Invalid token confirmation: " + ex.getMessage());
			log.trace("Confirmation error", ex);
		}
		catch (IOException ex) {
			log.error("Confirmation redirection error", ex);
		}
	}

	private String buildRedirectUri(AppUser user, String baseUrl) {
		StringBuilder sb = new StringBuilder(baseUrl);
		if (!confirmationRedirectUri.contains("?")) {
			sb.append("?");
		}
		else {
			sb.append("&");
		}
		sb.append("userId=").append(user.getId());
		return sb.toString();
	}
}

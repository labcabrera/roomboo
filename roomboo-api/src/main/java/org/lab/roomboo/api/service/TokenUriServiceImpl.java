package org.lab.roomboo.api.service;

import java.net.URI;

import org.lab.roomboo.api.controller.AppUserController;
import org.lab.roomboo.api.controller.BookingController;
import org.lab.roomboo.core.service.TokenUriService;
import org.lab.roomboo.domain.model.ReserveConfirmationToken;
import org.lab.roomboo.domain.model.UserConfirmationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

@Component
public class TokenUriServiceImpl implements TokenUriService {

	@Override
	public void processUri(ReserveConfirmationToken token) {
		URI confirmationUri = MvcUriComponentsBuilder.fromController(BookingController.class).path("/accept/{token}")
			.buildAndExpand(token.getToken()).toUri();
		URI cancelationUri = MvcUriComponentsBuilder.fromController(BookingController.class).path("/decline/{token}")
			.buildAndExpand(token.getToken()).toUri();
		token.setConfirmationUri(confirmationUri.toString());
		token.setCancelationUri(cancelationUri.toString());
	}

	@Override
	public void processUri(UserConfirmationToken token) {
		URI confirmationUri = MvcUriComponentsBuilder.fromController(AppUserController.class).path("/accept/{token}")
			.buildAndExpand(token.getToken()).toUri();
		token.setConfirmationUri(confirmationUri.toString());
	}

}

package org.lab.roomboo.core.integration.handler;

import java.time.LocalDateTime;
import java.util.Map;

import org.lab.roomboo.domain.model.AppUser;
import org.springframework.integration.handler.GenericHandler;
import org.springframework.stereotype.Component;

@Component
public class UserActivationHandler implements GenericHandler<AppUser> {

	@Override
	public Object handle(AppUser payload, Map<String, Object> headers) {
		payload.setActivation(LocalDateTime.now());
		return payload;
	}

}

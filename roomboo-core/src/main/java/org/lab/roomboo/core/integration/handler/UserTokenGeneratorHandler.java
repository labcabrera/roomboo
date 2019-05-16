package org.lab.roomboo.core.integration.handler;

import java.time.LocalDateTime;

import org.lab.roomboo.core.component.TokenGenerator;
import org.lab.roomboo.core.service.TokenUriService;
import org.lab.roomboo.domain.model.AppUser;
import org.lab.roomboo.domain.model.UserConfirmationToken;
import org.lab.roomboo.domain.repository.UserConfirmationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.handler.GenericHandler;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UserTokenGeneratorHandler implements GenericHandler<AppUser> {

	@Autowired
	private UserConfirmationTokenRepository tokenRepository;

	@Autowired
	private TokenGenerator tokenGenerator;

	@Autowired
	private TokenUriService tokenUriService;

	@Value("${app.env.token.user-register.expiration:1440}")
	private Integer tokenExpiration;

	@Override
	public Object handle(AppUser payload, MessageHeaders headers) {
		String userId = payload.getId();
		log.debug("Generating user confirmation token for user {}", userId);

		tokenRepository.deleteByUserId(userId);

		UserConfirmationToken token = UserConfirmationToken.builder()
			.created(LocalDateTime.now())
			.expiration(LocalDateTime.now().plusMinutes(tokenExpiration))
			.user(payload)
			.token(tokenGenerator.generate())
			.build();
		tokenUriService.processUri(token);
		tokenRepository.save(token);

		return payload;
	}
}
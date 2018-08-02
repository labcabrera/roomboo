package org.lab.roomboo.core.model.event.listener;

import java.time.LocalDateTime;

import org.lab.roomboo.core.component.TokenGenerator;
import org.lab.roomboo.core.model.event.AppUserCreatedEvent;
import org.lab.roomboo.core.service.TokenUriService;
import org.lab.roomboo.domain.model.AppUser;
import org.lab.roomboo.domain.model.UserConfirmationToken;
import org.lab.roomboo.domain.repository.UserConfirmationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Order(EventListenerOrder.UserCreated.Token)
@Slf4j
public class UserCreatedTokenListener implements ApplicationListener<AppUserCreatedEvent> {

	@Autowired
	private UserConfirmationTokenRepository tokenRepository;

	@Autowired
	private TokenGenerator tokenGenerator;

	@Autowired
	private TokenUriService tokenUriService;

	@Value("${app.env.token.user-register.expiration:1440}")
	private Integer tokenExpiration;

	@Override
	public void onApplicationEvent(AppUserCreatedEvent event) {
		log.debug("Creating new confirmation token for user {}", event.getUser());
		String userId = event.getUser().getId();
		UserConfirmationToken token = new UserConfirmationToken();
		token.setCreated(LocalDateTime.now());
		token.setExpiration(LocalDateTime.now().plusMinutes(tokenExpiration));
		token.setUser(AppUser.builder().id(userId).build());
		token.setToken(tokenGenerator.generate());
		tokenUriService.processUri(token);
		tokenRepository.save(token);
	}

}

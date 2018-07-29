package org.lab.roomboo.core.notification.impl;

import java.time.LocalDateTime;

import org.lab.roomboo.core.component.TokenGenerator;
import org.lab.roomboo.core.notification.ReserveCreatedProcessor.NotificationOrder;
import org.lab.roomboo.core.notification.UserCreatedProcessor;
import org.lab.roomboo.core.service.TokenUriService;
import org.lab.roomboo.domain.model.AppUser;
import org.lab.roomboo.domain.model.UserConfirmationToken;
import org.lab.roomboo.domain.repository.UserConfirmationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
@Order(NotificationOrder.TokenCreation)
public class UserCreatedTokenProcessor implements UserCreatedProcessor {

	@Autowired
	private UserConfirmationTokenRepository tokenRepository;

	@Autowired
	private TokenGenerator tokenGenerator;

	@Autowired
	private TokenUriService tokenUriService;

	@Value("${app.env.token.user-register.expiration:1440}")
	private Integer tokenExpiration;

	@Override
	public void process(AppUser source) {
		UserConfirmationToken token = new UserConfirmationToken();
		token.setCreated(LocalDateTime.now());
		token.setExpiration(LocalDateTime.now().plusMinutes(tokenExpiration));
		token.setUser(AppUser.builder().id(source.getId()).build());
		token.setToken(tokenGenerator.generate());
		tokenUriService.processUri(token);
		tokenRepository.save(token);
	}

}

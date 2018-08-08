package org.lab.roomboo.core.service;

import java.time.LocalDateTime;

import org.lab.roomboo.core.event.AppUserConfirmedEvent;
import org.lab.roomboo.core.event.AppUserCreatedEvent;
import org.lab.roomboo.core.model.AppUserRegisterRequest;
import org.lab.roomboo.domain.exception.UserConfirmationException;
import org.lab.roomboo.domain.exception.UserConfirmationException.ErrorType;
import org.lab.roomboo.domain.model.AppUser;
import org.lab.roomboo.domain.model.UserConfirmationToken;
import org.lab.roomboo.domain.repository.AppUserRepository;
import org.lab.roomboo.domain.repository.UserConfirmationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SignUpService {

	@Autowired
	private AppUserRepository repository;

	@Autowired
	private UserConfirmationTokenRepository tokenRepository;

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	public AppUser register(AppUserRegisterRequest request) {
		log.debug("Creating new app user");
		AppUser entity = AppUser.builder() //@formatter:off
			.email(request.getEmail())
			.name(request.getName())
			.lastName(request.getLastName())
			.created(LocalDateTime.now())
			.build(); //@formatter:on
		AppUser inserted = repository.insert(entity);
		log.debug("Triggering AppUserCreatedEvent");
		applicationEventPublisher.publishEvent(new AppUserCreatedEvent(this, inserted));
		return inserted;
	}

	public AppUser processConfirmationToken(String token) {
		UserConfirmationToken tokenEntity = tokenRepository.findByToken(token)
			.orElseThrow(() -> new UserConfirmationException(ErrorType.INVALID_TOKEN));
		String userId = tokenEntity.getUser().getId();
		AppUser user = repository.findById(userId)
			.orElseThrow(() -> new UserConfirmationException(ErrorType.INVALID_USER));
		if (user.getActivation() != null) {
			throw new UserConfirmationException(ErrorType.USER_ALREADY_ACTIVE);
		}
		user.setActivation(LocalDateTime.now());
		repository.save(user);
		tokenRepository.deleteById(tokenEntity.getId());
		applicationEventPublisher.publishEvent(new AppUserConfirmedEvent(this, user));
		return user;
	}
}

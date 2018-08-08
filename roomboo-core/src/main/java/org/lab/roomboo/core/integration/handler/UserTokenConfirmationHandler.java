package org.lab.roomboo.core.integration.handler;

import java.time.LocalDateTime;
import java.util.Map;

import org.lab.roomboo.domain.exception.UserConfirmationException;
import org.lab.roomboo.domain.exception.UserConfirmationException.ErrorType;
import org.lab.roomboo.domain.model.AppUser;
import org.lab.roomboo.domain.model.UserConfirmationToken;
import org.lab.roomboo.domain.repository.AppUserRepository;
import org.lab.roomboo.domain.repository.UserConfirmationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.handler.GenericHandler;
import org.springframework.stereotype.Component;

@Component
public class UserTokenConfirmationHandler implements GenericHandler<String> {

	@Autowired
	private AppUserRepository repository;

	@Autowired
	private UserConfirmationTokenRepository tokenRepository;

	@Override
	public Object handle(String token, Map<String, Object> headers) {
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
		return user;
	}

}

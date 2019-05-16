package org.lab.roomboo.core.integration.handler;

import java.time.LocalDateTime;

import org.lab.roomboo.domain.exception.EntityNotFoundException;
import org.lab.roomboo.domain.model.Reserve;
import org.lab.roomboo.domain.model.ReserveConfirmationToken;
import org.lab.roomboo.domain.repository.ReserveConfirmationTokenRepository;
import org.lab.roomboo.domain.repository.ReserveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.handler.GenericHandler;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

@Component
public class ReserveTokenConfirmationHandler implements GenericHandler<String> {

	@Autowired
	private ReserveRepository repository;

	@Autowired
	private ReserveConfirmationTokenRepository tokenRepository;

	@Override
	public Reserve handle(String token, MessageHeaders headers) {
		ReserveConfirmationToken tokenEntity = tokenRepository.findByToken(token)
			.orElseThrow(() -> new EntityNotFoundException("Missing confirmation token"));
		// TODO validate state
		Reserve reserve = repository.findById(tokenEntity.getReserve().getId())
			.orElseThrow(() -> new EntityNotFoundException("Missing reserve"));
		reserve.setConfirmed(LocalDateTime.now());
		repository.save(reserve);
		tokenRepository.delete(tokenEntity);
		return reserve;
	}
}

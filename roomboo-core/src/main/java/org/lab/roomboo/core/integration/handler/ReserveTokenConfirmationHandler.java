package org.lab.roomboo.core.integration.handler;

import java.time.LocalDateTime;
import java.util.Map;

import org.lab.roomboo.domain.model.Reserve;
import org.lab.roomboo.domain.model.ReserveConfirmationToken;
import org.lab.roomboo.domain.repository.ReserveConfirmationTokenRepository;
import org.lab.roomboo.domain.repository.ReserveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.handler.GenericHandler;
import org.springframework.stereotype.Component;

@Component
public class ReserveTokenConfirmationHandler implements GenericHandler<String> {

	@Autowired
	private ReserveRepository repository;

	@Autowired
	private ReserveConfirmationTokenRepository tokenRepository;

	@Override
	public Object handle(String token, Map<String, Object> headers) {
		ReserveConfirmationToken tokenEntity = tokenRepository.findByToken(token).get();
		// TODO validate state
		Reserve reserve = repository.findById(tokenEntity.getReserve().getId()).get();
		reserve.setConfirmed(LocalDateTime.now());
		repository.save(reserve);
		tokenRepository.delete(tokenEntity);
		return reserve;
	}
}

package org.lab.roomboo.core.integration.handler;

import java.time.LocalDateTime;

import org.lab.roomboo.core.component.TokenGenerator;
import org.lab.roomboo.core.service.TokenUriService;
import org.lab.roomboo.domain.model.Reserve;
import org.lab.roomboo.domain.model.ReserveConfirmationToken;
import org.lab.roomboo.domain.repository.ReserveConfirmationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.handler.GenericHandler;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

@Component
public class ReserveTokenGeneratorHandler implements GenericHandler<Reserve> {

	@Autowired
	private ReserveConfirmationTokenRepository tokenRepository;

	@Autowired
	private TokenUriService tokenUriService;

	@Autowired
	private TokenGenerator tokenGenerator;

	@Value("${app.env.token.reserve.expiration:1440}")
	private Integer tokenExpiration;

	@Override
	public Object handle(Reserve payload, MessageHeaders headers) {
		ReserveConfirmationToken token = new ReserveConfirmationToken();
		token.setCreated(LocalDateTime.now());
		token.setExpiration(LocalDateTime.now().plusMinutes(tokenExpiration));
		token.setReserve(payload);
		token.setToken(tokenGenerator.generate());
		tokenUriService.processUri(token);
		tokenRepository.save(token);
		return payload;
	}

}

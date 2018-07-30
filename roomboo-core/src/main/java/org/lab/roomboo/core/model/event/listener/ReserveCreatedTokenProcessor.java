package org.lab.roomboo.core.model.event.listener;

import java.time.LocalDateTime;

import org.lab.roomboo.core.component.TokenGenerator;
import org.lab.roomboo.core.model.event.ReserveCreatedEvent;
import org.lab.roomboo.core.model.event.ReserveCreatedEvent.EventOrder;
import org.lab.roomboo.core.service.TokenUriService;
import org.lab.roomboo.domain.model.Reserve;
import org.lab.roomboo.domain.model.ReserveConfirmationToken;
import org.lab.roomboo.domain.repository.ReserveConfirmationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
@Order(EventOrder.TokenCreation)
public class ReserveCreatedTokenProcessor implements ApplicationListener<ReserveCreatedEvent> {

	@Autowired
	private ReserveConfirmationTokenRepository tokenRepository;

	@Autowired
	private TokenGenerator tokenGenerator;

	@Autowired
	private TokenUriService tokenUriService;

	@Value("${app.env.token.reserve.expiration:1440}")
	private Integer tokenExpiration;

	@Override
	public void onApplicationEvent(ReserveCreatedEvent event) {
		ReserveConfirmationToken token = new ReserveConfirmationToken();
		token.setCreated(LocalDateTime.now());
		token.setExpiration(LocalDateTime.now().plusMinutes(tokenExpiration));
		token.setReserve(Reserve.builder().id(event.getReserve().getId()).build());
		token.setToken(tokenGenerator.generate());
		tokenUriService.processUri(token);
		tokenRepository.save(token);
	}

}

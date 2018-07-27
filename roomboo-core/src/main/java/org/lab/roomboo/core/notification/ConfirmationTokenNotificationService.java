package org.lab.roomboo.core.notification;

import java.time.LocalDateTime;

import org.lab.roomboo.core.component.TokenGenerator;
import org.lab.roomboo.core.notification.BookingNotificationService.NotificationOrder;
import org.lab.roomboo.core.service.TokenUriService;
import org.lab.roomboo.domain.model.Reserve;
import org.lab.roomboo.domain.model.ReserveConfirmationToken;
import org.lab.roomboo.domain.repository.ReserveConfirmationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
@Order(NotificationOrder.TokenCreation)
public class ConfirmationTokenNotificationService implements BookingNotificationService {

	@Autowired
	private ReserveConfirmationTokenRepository tokenRepository;

	@Autowired
	private TokenGenerator tokenGenerator;

	@Autowired
	private TokenUriService tokenUriService;

	@Value("${app.env.token.reserve.expiration:1440}")
	private Integer tokenExpiration;

	@Override
	public void reserveCreated(Reserve reserve) {
		ReserveConfirmationToken token = new ReserveConfirmationToken();
		token.setCreated(LocalDateTime.now());
		token.setExpiration(LocalDateTime.now().plusMinutes(tokenExpiration));
		token.setReserve(Reserve.builder().id(reserve.getId()).build());
		token.setToken(tokenGenerator.generate());
		tokenUriService.processUri(token);
		tokenRepository.save(token);
	}

}

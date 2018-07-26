package org.lab.roomboo.core.service;

import java.time.LocalDateTime;
import java.util.List;

import org.lab.roomboo.core.model.BookingRequest;
import org.lab.roomboo.core.notification.BookingNotificationService;
import org.lab.roomboo.domain.exception.TokenConfirmationException;
import org.lab.roomboo.domain.model.AppUser;
import org.lab.roomboo.domain.model.Reserve;
import org.lab.roomboo.domain.model.ReserveConfirmationToken;
import org.lab.roomboo.domain.model.Room;
import org.lab.roomboo.domain.repository.ReserveConfirmationTokenRepository;
import org.lab.roomboo.domain.repository.ReserveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BookingService {

	@Autowired
	private ReserveRepository reserveRepository;

	@Autowired
	private ReserveCodeGenerator codeGenerator;

	@Autowired
	private ReserveConfirmationTokenRepository tokenRepository;

	@Autowired
	private List<BookingNotificationService> notificationServices;

	public Reserve processBookingRequest(BookingRequest request) {
		// // TODO check dates
		Reserve reserve = new Reserve();
		reserve.setUser(AppUser.builder().id(request.getUserId()).build());
		reserve.setRoom(Room.builder().id(request.getRoomId()).build());
		reserve.setFrom(request.getFrom());
		reserve.setTo(request.getTo());
		reserve.setName(request.getName());
		reserve.setCode(codeGenerator.generate());
		Reserve inserted = reserveRepository.insert(reserve);
		notificationServices.forEach(x -> {
			log.debug("Processing notification with {}", x.getClass().getSimpleName());
			x.reserveCreated(inserted);
		});
		return inserted;
	}

	public Reserve processTokenConfirmation(String token) {
		ReserveConfirmationToken tokenEntity = tokenRepository.findByToken(token).orElse(null);
		LocalDateTime now = LocalDateTime.now();
		if (tokenEntity == null) {
			throw new TokenConfirmationException("Invalid token");
		}
		if (tokenEntity.getExpiration().isBefore(now)) {
			throw new TokenConfirmationException("Token expired");
		}
		Reserve reserve = reserveRepository.findById(tokenEntity.getReserve().getId()).orElse(null);
		if (reserve == null) {
			throw new TokenConfirmationException("Invalid token reserve");
		}
		if (reserve.getConfirmed() != null) {
			throw new TokenConfirmationException("Reserve already confirmed");
		}
		reserve.setConfirmed(now);
		reserveRepository.save(reserve);
		tokenRepository.delete(tokenEntity);
		return reserve;
	}

}

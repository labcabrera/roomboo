package org.lab.roomboo.core.service;

import java.time.LocalDateTime;
import java.util.List;

import org.lab.roomboo.core.model.BookingRequest;
import org.lab.roomboo.core.notification.BookingNotificationService;
import org.lab.roomboo.domain.exception.ReserveConfirmationException;
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

	public Reserve processReserveConfirmationByToken(String token) {
		ReserveConfirmationToken tokenEntity = tokenRepository.findByToken(token).orElse(null);
		if (tokenEntity == null) {
			throw new ReserveConfirmationException("Invalid token");
		}
		if (tokenEntity.getExpiration().isBefore(LocalDateTime.now())) {
			throw new ReserveConfirmationException("Token expired");
		}
		Reserve reserve = processReserveConfirmation(tokenEntity.getReserve().getId());
		reserveRepository.save(reserve);
		tokenRepository.delete(tokenEntity);
		return reserve;
	}

	public Reserve processCodeReserveConfirmationByCode(String reserveId, String code) {
		Reserve reserve = reserveRepository.findById(reserveId)
			.orElseThrow(() -> new ReserveConfirmationException("Invalid reserve identifier"));
		if (!reserve.getCode().equals(code)) {
			throw new ReserveConfirmationException("Invalid code");
		}
		return processReserveConfirmation(reserveId);
	}

	private Reserve processReserveConfirmation(String reserveId) {
		// TODO check dates
		// TODO check room not locked
		Reserve reserve = reserveRepository.findById(reserveId).orElse(null);
		if (reserve == null) {
			throw new ReserveConfirmationException("Invalid token reserve");
		}
		if (reserve.getConfirmed() != null) {
			throw new ReserveConfirmationException("Reserve already confirmed");
		}
		reserve.setConfirmed(LocalDateTime.now());
		return reserveRepository.save(reserve);
	}

}

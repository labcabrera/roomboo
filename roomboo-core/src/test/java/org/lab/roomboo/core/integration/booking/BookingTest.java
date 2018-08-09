package org.lab.roomboo.core.integration.booking;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import org.junit.Assume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lab.roomboo.core.RoombooCoreConfig;
import org.lab.roomboo.core.integration.IntegrationTest;
import org.lab.roomboo.core.integration.gateway.BookingGateway;
import org.lab.roomboo.core.model.BookingRequest;
import org.lab.roomboo.core.service.ReserveService;
import org.lab.roomboo.domain.model.Reserve;
import org.lab.roomboo.domain.model.ReserveConfirmationToken;
import org.lab.roomboo.domain.model.Room;
import org.lab.roomboo.domain.repository.ReserveConfirmationTokenRepository;
import org.lab.roomboo.domain.repository.ReserveRepository;
import org.lab.roomboo.domain.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RoombooCoreConfig.class)
public class BookingTest extends IntegrationTest {

	@Autowired
	private BookingGateway gateway;

	@Autowired
	private RoomRepository roomRepository;

	@Autowired
	private ReserveService reserveService;

	@Autowired
	private ReserveRepository reserveRepository;

	@Autowired
	private ReserveConfirmationTokenRepository tokenRepository;

	@Test
	public void test() {
		String roomId = "milankundera";

		Optional<Room> optionalRoom = roomRepository.findById(roomId);
		Assume.assumeTrue(optionalRoom.isPresent());

		LocalDateTime from = LocalDateTime.now().plusDays(1).with(LocalTime.parse("10:00:00.000"));
		LocalDateTime to = LocalDateTime.now().plusDays(1).with(LocalTime.parse("10:30:00.000"));

		Optional<Reserve> current = reserveService.findInRange(roomId, from, to);
		if (current.isPresent()) {
			reserveRepository.delete(current.get());
		}

		BookingRequest request = BookingRequest.builder() //@formatter:off
			.name("Test")
			.roomId(roomId)
			.userId("labcabrera")
			.from(from)
			.to(to)
			.build(); //@formatter:on
		Reserve reserve = gateway.processBookingRequest(request);
		awaitTaskTermination();

		ReserveConfirmationToken token = tokenRepository.findByReserveId(reserve.getId()).get();

		gateway.processReserveConfirmationByToken(token.getToken());

	}

}

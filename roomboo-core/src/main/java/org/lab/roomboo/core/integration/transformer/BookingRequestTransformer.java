package org.lab.roomboo.core.integration.transformer;

import java.time.LocalDateTime;

import org.lab.roomboo.core.component.ReserveCodeGenerator;
import org.lab.roomboo.core.model.BookingRequest;
import org.lab.roomboo.domain.model.AppUser;
import org.lab.roomboo.domain.model.Reserve;
import org.lab.roomboo.domain.model.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.transformer.GenericTransformer;
import org.springframework.stereotype.Component;

@Component
public class BookingRequestTransformer implements GenericTransformer<BookingRequest, Reserve> {

	@Autowired
	private ReserveCodeGenerator reserveCodeGenerator;

	@Override
	public Reserve transform(BookingRequest request) {
		Reserve reserve = new Reserve();
		reserve.setUser(AppUser.builder().id(request.getUserId()).build());
		reserve.setRoom(Room.builder().id(request.getRoomId()).build());
		reserve.setFrom(request.getFrom());
		reserve.setTo(request.getTo());
		reserve.setCreated(LocalDateTime.now());
		reserve.setName(request.getName());
		reserve.setCode(reserveCodeGenerator.generate());
		return reserve;
	}

}

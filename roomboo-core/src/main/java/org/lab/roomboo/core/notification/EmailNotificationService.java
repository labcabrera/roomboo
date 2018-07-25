package org.lab.roomboo.core.notification;

import org.lab.roomboo.domain.model.Reserve;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailNotificationService implements BookingNotificationService {

	@Override
	public void reserveCreated(Reserve reserve) {
		log.warn("Not implemented: email notification");
	}

}

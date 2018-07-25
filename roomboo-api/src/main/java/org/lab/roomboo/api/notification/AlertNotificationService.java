package org.lab.roomboo.api.notification;

import org.lab.roomboo.domain.model.Alert;
import org.lab.roomboo.domain.model.Reserve;
import org.lab.roomboo.domain.repository.AlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlertNotificationService implements BookingNotificationService {

	@Autowired
	private AlertRepository alertRepository;

	@Override
	public void reserveCreated(Reserve reserve) {
		alertRepository.save(build(reserve));
	}

	private Alert build(Reserve reserve) {
		//@formatter:off
		return Alert.builder()
			.message("")
			.build();
		//@formatter:on
	}

}

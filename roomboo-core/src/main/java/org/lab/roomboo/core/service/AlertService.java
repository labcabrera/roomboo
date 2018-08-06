package org.lab.roomboo.core.service;

import java.time.LocalDateTime;

import org.lab.roomboo.domain.model.Alert;
import org.lab.roomboo.domain.model.Alert.AlertType;
import org.lab.roomboo.domain.repository.AlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AlertService {

	@Autowired
	private AlertRepository repository;

	public Alert create(String subject, String message) {
		//@formatter:off
		Alert alert = Alert.builder()
			.created(LocalDateTime.now())
			.alertType(AlertType.INFO)
			.subject(subject)
			.message(message)
			.build();
		//@formatter:on
		return repository.save(alert);
	}
}

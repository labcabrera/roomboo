package org.lab.roomboo.core.event.listener;

import java.time.LocalDateTime;

import org.lab.roomboo.core.event.AppUserConfirmedEvent;
import org.lab.roomboo.domain.model.Alert;
import org.lab.roomboo.domain.model.Alert.AlertType;
import org.lab.roomboo.domain.repository.AlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Order(EventListenerOrder.UserCreated.Alert)
@Slf4j
public class UserConfirmedAlertListener implements ApplicationListener<AppUserConfirmedEvent> {

	@Autowired
	private AlertRepository alertRepository;

	@Async
	@Override
	public void onApplicationEvent(AppUserConfirmedEvent event) {
		//@formatter:off
		Alert alert = Alert.builder()
			.created(LocalDateTime.now())
			.type(AlertType.INFO)
			.subject("User confirmed")
			.message(String.format("Confirmed user %s", event.getUser().getEmail()))
			.build();
		//@formatter:on

		alertRepository.insert(alert);
		log.debug("Created alert {}", alert);
	}

}

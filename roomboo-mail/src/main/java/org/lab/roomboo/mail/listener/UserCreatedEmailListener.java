package org.lab.roomboo.mail.listener;

import org.lab.roomboo.core.model.event.AppUserCreatedEvent;
import org.lab.roomboo.core.model.event.listener.EventListenerOrder;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Order(EventListenerOrder.UserCreated.EmailNotification)
@Slf4j
public class UserCreatedEmailListener implements ApplicationListener<AppUserCreatedEvent> {

	@Override
	public void onApplicationEvent(AppUserCreatedEvent event) {
		log.info("Sending user registration confirmation email");
		// TODO

	}

}

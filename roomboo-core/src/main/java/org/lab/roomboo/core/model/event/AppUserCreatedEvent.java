package org.lab.roomboo.core.model.event;

import org.lab.roomboo.domain.model.AppUser;
import org.springframework.context.ApplicationEvent;

import lombok.Getter;

@Getter
@SuppressWarnings("serial")
public class AppUserCreatedEvent extends ApplicationEvent {

	private final AppUser user;

	public AppUserCreatedEvent(Object source, AppUser user) {
		super(source);
		this.user = user;
	}

}

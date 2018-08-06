package org.lab.roomboo.core.event;

import org.lab.roomboo.domain.model.AppUser;
import org.springframework.context.ApplicationEvent;

import lombok.Getter;

@SuppressWarnings("serial")
public class AppUserConfirmedEvent extends ApplicationEvent {

	@Getter
	private final AppUser user;

	public AppUserConfirmedEvent(Object source, AppUser user) {
		super(source);
		this.user = user;
	}

}

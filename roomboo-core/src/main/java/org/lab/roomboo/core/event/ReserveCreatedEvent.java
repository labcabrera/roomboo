package org.lab.roomboo.core.event;

import org.lab.roomboo.domain.model.Reserve;
import org.springframework.context.ApplicationEvent;

import lombok.Getter;

@Getter
@SuppressWarnings("serial")
public class ReserveCreatedEvent extends ApplicationEvent {



	private final Reserve reserve;

	public ReserveCreatedEvent(Object source, Reserve reserve) {
		super(source);
		this.reserve = reserve;
	}

}
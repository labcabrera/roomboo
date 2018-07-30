package org.lab.roomboo.core.model.event;

import org.lab.roomboo.domain.model.Reserve;
import org.springframework.context.ApplicationEvent;

import lombok.Getter;

@Getter
@SuppressWarnings("serial")
public class ReserveCreatedEvent extends ApplicationEvent {

	public interface EventOrder {
		int TokenCreation = 1010;
		int AlertCreation = 1020;
		int EmailCreation = 1030;
	}

	private final Reserve reserve;

	public ReserveCreatedEvent(Object source, Reserve reserve) {
		super(source);
		this.reserve = reserve;
	}

}
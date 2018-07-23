package org.lab.roomboo.domain.exception;

import lombok.Getter;

@Getter
@SuppressWarnings("serial")
public class EntityNotFoundException extends RoombooException {

	private final Class<?> type;
	private final String id;

	public EntityNotFoundException(Class<?> type, String id) {
		super(String.format("Entity '%s' with id '%s' not found", type.getName(), id));
		this.type = type;
		this.id = id;
	}

}

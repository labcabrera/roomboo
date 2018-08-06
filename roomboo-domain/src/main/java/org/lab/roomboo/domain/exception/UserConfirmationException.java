package org.lab.roomboo.domain.exception;

import lombok.Getter;

@SuppressWarnings("serial")
public class UserConfirmationException extends RoombooException {

	@Getter
	private final ErrorType errorType;

	public UserConfirmationException(ErrorType errorType) {
		super("User token confirmation error");
		this.errorType = errorType;
	}

	public enum ErrorType {
		INVALID_TOKEN, INVALID_USER, USER_ALREADY_ACTIVE
	}

}

package org.lab.roomboo.domain.exception;

@SuppressWarnings("serial")
public class UserConfirmationException extends RoombooException {

	public UserConfirmationException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserConfirmationException(String message) {
		super(message);
	}

	public UserConfirmationException(Throwable cause) {
		super(cause);
	}

}

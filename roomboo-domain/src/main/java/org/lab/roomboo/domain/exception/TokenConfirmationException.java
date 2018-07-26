package org.lab.roomboo.domain.exception;

@SuppressWarnings("serial")
public class TokenConfirmationException extends RoombooException {

	public TokenConfirmationException(String message, Throwable cause) {
		super(message, cause);
	}

	public TokenConfirmationException(String message) {
		super(message);
	}

	public TokenConfirmationException(Throwable cause) {
		super(cause);
	}

}

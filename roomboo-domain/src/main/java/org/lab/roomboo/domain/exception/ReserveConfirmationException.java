package org.lab.roomboo.domain.exception;

@SuppressWarnings("serial")
public class ReserveConfirmationException extends RoombooException {

	public ReserveConfirmationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ReserveConfirmationException(String message) {
		super(message);
	}

	public ReserveConfirmationException(Throwable cause) {
		super(cause);
	}

}

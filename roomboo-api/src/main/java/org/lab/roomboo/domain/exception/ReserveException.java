package org.lab.roomboo.domain.exception;

@SuppressWarnings("serial")
public class ReserveException extends RoombooException {

	public ReserveException(String message, Throwable cause) {
		super(message, cause);
	}

	public ReserveException(String message) {
		super(message);
	}

	public ReserveException(Throwable cause) {
		super(cause);
	}

}

package org.lab.roomboo.exception;

@SuppressWarnings("serial")
public class RoombooException extends RuntimeException {

	public RoombooException(String message, Throwable cause) {
		super(message, cause);
	}

	public RoombooException(String message) {
		super(message);
	}

	public RoombooException(Throwable cause) {
		super(cause);
	}

}

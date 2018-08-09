package org.lab.roomboo.domain.exception;

import org.springframework.validation.BindingResult;

import lombok.Getter;

@SuppressWarnings("serial")
public class RoombooValidationError extends RoombooException {

	@Getter
	private final BindingResult bindingResult;

	public RoombooValidationError(String message, Throwable cause, BindingResult bindingResult) {
		super(message, cause);
		this.bindingResult = bindingResult;
	}

	public RoombooValidationError(String message, BindingResult bindingResult) {
		super(message);
		this.bindingResult = bindingResult;
	}

	public RoombooValidationError(Throwable cause, BindingResult bindingResult) {
		super(cause);
		this.bindingResult = bindingResult;
	}

}

package org.lab.roomboo.core.integration.handler;

import java.util.Map;

import org.lab.roomboo.domain.exception.RoombooValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.handler.GenericHandler;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

@Component
public class PayloadValidatorHandler implements GenericHandler<Object> {

	/**
	 * Header to check whether the message has been previously validated and to avoid making repeated validations.
	 */
	public static final String HEADER_PRE_VALIDATED = "x-pre-validated";

	@Autowired
	private Validator validator;

	@Override
	public Object handle(Object payload, Map<String, Object> headers) {
		if (!isPreValidatedMessage(headers)) {
			BindingResult bindingResult = new BeanPropertyBindingResult(payload, "payload");
			validator.validate(payload, bindingResult);
			if (bindingResult.hasErrors()) {
				throw new RoombooValidationError("Payload validation error", bindingResult);
			}
		}
		return payload;
	}

	private boolean isPreValidatedMessage(Map<String, Object> headers) {
		return "true".equals(headers.getOrDefault(HEADER_PRE_VALIDATED, "false"));
	}

}

package org.lab.roomboo.core.integration.transformer;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.core.GenericSelector;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ValidationFilter implements GenericSelector<Object> {

	@Autowired
	private Validator validator;

	@Override
	public boolean accept(Object source) {
		Set<ConstraintViolation<Object>> result = validator.validate(source);
		if (result.isEmpty()) {
			return true;
		}
		log.debug("Invalid object");
		return false;
	}

}

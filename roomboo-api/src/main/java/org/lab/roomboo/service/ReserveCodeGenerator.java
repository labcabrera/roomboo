package org.lab.roomboo.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

@Component
public class ReserveCodeGenerator {

	public String randomCode() {
		return RandomStringUtils.randomNumeric(6);
	}

}

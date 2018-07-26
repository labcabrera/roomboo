package org.lab.roomboo.core.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

@Component
public class TokenGenerator {

	public String generate() {
		return RandomStringUtils.randomAlphanumeric(64);
	}
}

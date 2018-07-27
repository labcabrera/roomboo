package org.lab.roomboo.core.component;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

@Component
public class ReserveCodeGenerator {

	public String generate() {
		return RandomStringUtils.randomNumeric(6);
	}

}

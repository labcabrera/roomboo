package org.lab.roomboo.core.signup;

import org.apache.commons.lang3.RandomStringUtils;
import org.lab.roomboo.core.integration.gateway.SignUpGateway;
import org.lab.roomboo.core.model.SignUpRequest;
import org.lab.roomboo.domain.repository.AppUserRepository;
import org.lab.roomboo.domain.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public abstract class SignUpTest {

	@Autowired
	protected SignUpGateway gateway;

	@Autowired
	protected ThreadPoolTaskExecutor executor;

	@Autowired
	protected CompanyRepository companyRepository;

	@Autowired
	protected AppUserRepository appUserRepository;

	protected SignUpRequest getValidSignUpRequest(String companyId) {
		return SignUpRequest.builder() //@formatter:off
			.email(getValidEmail(companyId))
			.name("Luis")
			.lastName("Cabrera")
			.companyId(companyId)
			.build(); //@formatter:on
	}

	private String getValidEmail(String companyId) {
		String email;
		while (true) {
			email = "lab.cabrera-" + RandomStringUtils.randomAlphabetic(6) + "@gmail.com";
			if (!appUserRepository.findByEmail(email, companyId).isPresent()) {
				return email;
			}
		}
	}

	public void awaitTaskTermination() {
		long check = System.currentTimeMillis() + 1000000;
		while (System.currentTimeMillis() < check) {
			if (executor.getActiveCount() == 0) {
				break;
			}
			try {
				Thread.sleep(500);
			}
			catch (InterruptedException ignore) {
			}
		}
	}
}

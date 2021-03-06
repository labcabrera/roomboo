package org.lab.roomboo.core.integration.signup;

import org.apache.commons.lang3.RandomStringUtils;
import org.lab.roomboo.core.integration.IntegrationTest;
import org.lab.roomboo.core.integration.gateway.SignUpGateway;
import org.lab.roomboo.core.model.SignUpRequest;
import org.lab.roomboo.domain.repository.AppUserRepository;
import org.lab.roomboo.domain.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class SignUpTest extends IntegrationTest {

	@Autowired
	protected SignUpGateway gateway;

	@Autowired
	protected CompanyRepository companyRepository;

	@Autowired
	protected AppUserRepository appUserRepository;

	protected SignUpRequest getValidSignUpRequest(String companyId) {
		return SignUpRequest.builder()
			.email(getValidEmail(companyId))
			.name("Luis")
			.lastName("Cabrera")
			.companyId(companyId)
			.build();
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
}

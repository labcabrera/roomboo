package org.lab.roomboo.core.integration;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lab.roomboo.core.RoombooCoreConfig;
import org.lab.roomboo.core.integration.gateway.SignUpGateway;
import org.lab.roomboo.core.model.SignUpRequest;
import org.lab.roomboo.core.service.TokenUriService;
import org.lab.roomboo.domain.model.AppUser;
import org.lab.roomboo.domain.model.Company;
import org.lab.roomboo.domain.model.UserConfirmationToken;
import org.lab.roomboo.domain.repository.CompanyRepository;
import org.lab.roomboo.domain.repository.UserConfirmationTokenRepository;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RoombooCoreConfig.class)
public class SignUpEmailConfirmationTest {

	@Autowired
	private SignUpGateway gateway;

	@Autowired
	private ThreadPoolTaskExecutor executor;

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private UserConfirmationTokenRepository tokenRepository;

	@Test
	public void test() {
		Optional<Company> company = companyRepository.findById("roombooCompanyDemo01");
		Assume.assumeTrue(company.isPresent());
		Assert.assertEquals(Company.SignUpActivationMode.EMAIL, company.get().getSignUpActivationMode());

		SignUpRequest request = SignUpRequest.builder().email("lab.cabrera@gmail.com").name("Luis").lastName("Cabrera")
			.companyId(company.get().getId()).build();

		AppUser user = gateway.signUp(request);
		Assert.assertNull(user.getActivation());

		UserConfirmationToken token = tokenRepository.findByUserId(user.getId()).get();

		AppUser userAfterConfirmation = gateway.tokenConfirmation(token.getToken());
		Assert.assertNotNull(userAfterConfirmation.getActivation());

		executor.setAwaitTerminationSeconds(10);
		executor.shutdown();
	}

	@Configuration
	static class TestConfig {

		@Bean
		@ConditionalOnMissingBean
		TokenUriService mockTokenUriService() {
			TokenUriService mock = Mockito.mock(TokenUriService.class);
			return mock;
		}

	}

}

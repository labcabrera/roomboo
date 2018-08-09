package org.lab.roomboo.core.integration;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lab.roomboo.core.RoombooCoreConfig;
import org.lab.roomboo.core.model.SignUpRequest;
import org.lab.roomboo.core.service.TokenUriService;
import org.lab.roomboo.domain.model.AppUser;
import org.lab.roomboo.domain.model.Company;
import org.lab.roomboo.domain.model.UserConfirmationToken;
import org.lab.roomboo.domain.repository.UserConfirmationTokenRepository;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RoombooCoreConfig.class)
public class SignUpEmailConfirmationTest extends SignUpTest {

	@Autowired
	private UserConfirmationTokenRepository tokenRepository;

	@Test
	public void test() {
		String companyId = "roombooCompanyDemo01";

		Optional<Company> company = companyRepository.findById(companyId);
		Assume.assumeTrue(company.isPresent());
		Assert.assertEquals(Company.SignUpActivationMode.EMAIL, company.get().getSignUpActivationMode());

		SignUpRequest request = getValidSignUpRequest(companyId);

		AppUser user = gateway.signUp(request);
		Assert.assertNull(user.getActivation());

		// NOTE: wait until token is generated asynchronously
		executor.setAwaitTerminationSeconds(10);
		executor.shutdown();

		UserConfirmationToken token = tokenRepository.findByUserId(user.getId()).get();

		AppUser userAfterConfirmation = gateway.tokenConfirmation(token.getToken());
		Assert.assertNotNull(userAfterConfirmation.getActivation());

		executor.setAwaitTerminationSeconds(10);
		executor.shutdown();

		Assert.assertFalse(tokenRepository.findByUserId(user.getId()).isPresent());
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

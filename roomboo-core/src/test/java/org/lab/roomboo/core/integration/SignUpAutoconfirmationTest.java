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
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RoombooCoreConfig.class)
public class SignUpAutoconfirmationTest extends SignUpTest {

	@Test
	public void test() {
		String companyId = "roombooCompanyDemo02";

		Optional<Company> company = companyRepository.findById(companyId);

		Assume.assumeTrue(company.isPresent());
		Assert.assertEquals(Company.SignUpActivationMode.AUTO, company.get().getSignUpActivationMode());

		SignUpRequest request = getValidSignUpRequest(companyId);

		AppUser response = gateway.signUp(request);

		Assert.assertNotNull(response.getActivation());

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

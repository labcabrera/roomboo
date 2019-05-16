package org.lab.roomboo.core.integration.signup;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lab.roomboo.core.integration.gateway.SignUpGateway;
import org.lab.roomboo.core.model.SignUpRequest;
import org.lab.roomboo.core.service.TokenUriService;
import org.lab.roomboo.domain.model.AppUser;
import org.lab.roomboo.domain.model.Company;
import org.lab.roomboo.domain.model.UserConfirmationToken;
import org.lab.roomboo.domain.repository.AppUserRepository;
import org.lab.roomboo.domain.repository.CompanyRepository;
import org.lab.roomboo.domain.repository.UserConfirmationTokenRepository;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration
public class SignUpEmailConfirmationTest extends SignUpTest {

	private static final String COMPANY_ID = "roombooCompanyDemo01";

	@Autowired
	private UserConfirmationTokenRepository tokenRepository;

	@Test
	public void test() {

		Optional<Company> company = companyRepository.findById(COMPANY_ID);
		Assume.assumeTrue(company.isPresent());
		Assert.assertEquals(Company.SignUpActivationMode.EMAIL, company.get().getSignUpActivationMode());

		SignUpRequest request = getValidSignUpRequest(COMPANY_ID);

		AppUser user = gateway.signUp(request);
		awaitTaskTermination();

		Assert.assertNull(user.getActivation());

		UserConfirmationToken token = tokenRepository.findByUserId(user.getId()).get();

		AppUser userAfterConfirmation = gateway.tokenConfirmation(token.getToken());
		awaitTaskTermination();

		Assert.assertNotNull(userAfterConfirmation.getActivation());
	}

	@Configuration
	static class TestConfig {

		@Bean
		@ConditionalOnMissingBean
		TokenUriService mockTokenUriService() {
			TokenUriService mock = Mockito.mock(TokenUriService.class);
			return mock;
		}

		@Bean
		SignUpGateway signUpGateway() {
			SignUpGateway mock = mock(SignUpGateway.class);
			when(mock.signUp(any(SignUpRequest.class))).thenReturn(AppUser.builder()
				.id("userIdTest")
				.name("name")
				.email("test@test.me")
				.build());
			when(mock.tokenConfirmation(any(String.class))).thenReturn(AppUser.builder()
				.id("userIdTest")
				.name("name")
				.email("test@test.me")
				.activation(LocalDateTime.now().minusMonths(6))
				.build());
			return mock;
		}

		@Bean
		CompanyRepository companyRepository() {
			CompanyRepository mock = mock(CompanyRepository.class);
			when(mock.findById(COMPANY_ID)).thenReturn(Optional.of(Company.builder()
				.signUpActivationMode(Company.SignUpActivationMode.EMAIL)
				.build()));
			return mock;
		}

		@Bean
		UserConfirmationTokenRepository userConfirmationTokenRepository() {
			UserConfirmationTokenRepository mock = mock(UserConfirmationTokenRepository.class);
			when(mock.findByUserId(eq("userIdTest"))).thenReturn(Optional.of(UserConfirmationToken.builder()
				.user(AppUser.builder()
					.id("userIdTest")
					.name("name")
					.email("test@test.me")
					.build())
				.token("tokenTest")
				.build()));
			return mock;
		}

		@Bean
		ThreadPoolTaskExecutor threadPoolTaskExecutor() {
			return new ThreadPoolTaskExecutor();
		}

		@Bean
		AppUserRepository appUserRepository() {
			return Mockito.mock(AppUserRepository.class);
		}
	}

}

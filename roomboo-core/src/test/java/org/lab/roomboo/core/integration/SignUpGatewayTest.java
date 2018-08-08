package org.lab.roomboo.core.integration;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lab.roomboo.core.RoombooCoreConfig;
import org.lab.roomboo.core.integration.gateway.SignUpGateway;
import org.lab.roomboo.core.model.SignUpRequest;
import org.lab.roomboo.core.service.TokenUriService;
import org.lab.roomboo.domain.model.AppUser;
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
public class SignUpGatewayTest {

	@Autowired
	private SignUpGateway gateway;

	@Autowired
	private ThreadPoolTaskExecutor executor;

	@Test
	@Ignore
	public void testAutoConfirmation() {
		SignUpRequest request = SignUpRequest.builder().email("lab.cabrera@gmail.com").name("Luis").lastName("Cabrera")
			.companyId("roombooCompanyDemo02").build();
		AppUser response = gateway.signUp(request);
		System.out.println("--- Response:\n" + response + "\n---");
		executor.setAwaitTerminationSeconds(10);
		executor.shutdown();
	}

	@Test
	public void testEmailConfirmation() {
		SignUpRequest request = SignUpRequest.builder().email("lab.cabrera@gmail.com").name("Luis").lastName("Cabrera")
			.companyId("roombooCompanyDemo01").build();
		AppUser response = gateway.signUp(request);
		System.out.println("--- Response:\n" + response + "\n---");
		executor.setAwaitTerminationSeconds(10);
		executor.shutdown();
	}

	@Test
	@Ignore
	public void testKo() {
		SignUpRequest request = SignUpRequest.builder().email(null).name("Luis").lastName("Cabrera").build();
		Object response = gateway.signUp(request);
		System.out.println("--- Response:\n" + response + "\n---");

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

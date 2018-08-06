package org.lab.roomboo.core.integration;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lab.roomboo.core.RoombooCoreConfig;
import org.lab.roomboo.core.gateway.SignUpGateway;
import org.lab.roomboo.core.model.AppUserRegisterRequest;
import org.lab.roomboo.core.service.TokenUriService;
import org.lab.roomboo.domain.model.AppUser;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RoombooCoreConfig.class)
public class SignUpGatewayTest {

	@Autowired
	private SignUpGateway gateway;

	@Test
	public void test() {
		AppUserRegisterRequest request = AppUserRegisterRequest.builder().email("lab.cabrera@gmail.com")
			.displayName("Luis cabrera").build();
		AppUser response = gateway.signUp(request);
		System.out.println(response);

	}

	@Test
	@Ignore
	public void testKo() {
		AppUserRegisterRequest request = AppUserRegisterRequest.builder().email(null).displayName("Luis cabrera")
			.build();
		Object response = gateway.signUp(request);
		System.out.println(response);

	}

	@Configuration
	static class TestConfig {

		@Bean
		TokenUriService mockTokenUriService() {
			TokenUriService mock = Mockito.mock(TokenUriService.class);
			return mock;
		}

	}

}

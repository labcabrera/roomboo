package org.lab.roomboo.mail;

import java.util.Optional;

import org.lab.roomboo.domain.model.AppUser;
import org.lab.roomboo.domain.model.ReserveConfirmationToken;
import org.lab.roomboo.domain.repository.AppUserRepository;
import org.lab.roomboo.domain.repository.ReserveConfirmationTokenRepository;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(RoombooMailConfig.class)
public class RoombooMailTestConfig {

	@Bean
	AppUserRepository appUserRepository() {
		AppUser owner = AppUser.builder().id("1").displayName("Test name").email("test@localhost.org").build();
		AppUserRepository bean = Mockito.mock(AppUserRepository.class);
		Mockito.when(bean.findById("1")).thenReturn(Optional.of(owner));
		return bean;
	}

	@Bean
	ReserveConfirmationTokenRepository reserveConfirmationTokenRepository() {
		ReserveConfirmationToken token = new ReserveConfirmationToken();
		token.setUri("http://roomboo.org/reserves/confirmation/abcde");
		ReserveConfirmationTokenRepository bean = Mockito.mock(ReserveConfirmationTokenRepository.class);
		Mockito.when(bean.findValidToken(ArgumentMatchers.anyString(), ArgumentMatchers.any()))
			.thenReturn(Optional.of(token));
		return bean;
	}

}

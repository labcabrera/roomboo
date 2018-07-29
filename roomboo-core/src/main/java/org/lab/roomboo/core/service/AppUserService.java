package org.lab.roomboo.core.service;

import java.time.LocalDateTime;

import org.lab.roomboo.core.model.AppUserRegisterRequest;
import org.lab.roomboo.domain.model.AppUser;
import org.lab.roomboo.domain.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppUserService {

	@Autowired
	private AppUserRepository repository;

	public AppUser register(AppUserRegisterRequest request) {
		//@formatter:off
		AppUser entity = AppUser.builder()
			.email(request.getEmail())
			.displayName(request.getDisplayName())
			.created(LocalDateTime.now())
			.build();
		//@formatter:on
		entity.setCreated(LocalDateTime.now());
		// TODO send mail confirmation
		return repository.insert(entity);
	}

}

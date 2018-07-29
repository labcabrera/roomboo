package org.lab.roomboo.core.service;

import java.time.LocalDateTime;
import java.util.List;

import org.lab.roomboo.core.model.AppUserRegisterRequest;
import org.lab.roomboo.core.notification.UserCreatedProcessor;
import org.lab.roomboo.domain.model.AppUser;
import org.lab.roomboo.domain.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AppUserService {

	@Autowired
	private AppUserRepository repository;

	@Autowired
	private List<UserCreatedProcessor> processors;

	public AppUser register(AppUserRegisterRequest request) {
		log.debug("Creating new app user");

		//@formatter:off
		AppUser entity = AppUser.builder()
			.email(request.getEmail())
			.displayName(request.getDisplayName())
			.created(LocalDateTime.now())
			.build();
		//@formatter:on

		AppUser inserted = repository.insert(entity);
		processors.forEach(x -> {
			x.process(entity);
		});
		return inserted;
	}

}

package org.lab.roomboo.core.service;

import java.time.LocalDateTime;

import org.apache.commons.lang3.StringUtils;
import org.lab.roomboo.domain.model.AppUser;
import org.lab.roomboo.domain.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppUserService {

	@Autowired
	private AppUserRepository repository;

	public AppUser register(AppUser entity) {
		if (StringUtils.isBlank(entity.getDisplayName())) {
			entity.setDisplayName(entity.getEmail());
		}
		entity.setCreated(LocalDateTime.now());
		// TODO send mail confirmation
		return repository.insert(entity);
	}

}

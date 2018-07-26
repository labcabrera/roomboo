package org.lab.roomboo.api.bootstrap;

import org.lab.roomboo.api.bootstrap.DataInitializer.InitializationOrder;
import org.lab.roomboo.domain.model.AppUser;
import org.lab.roomboo.domain.repository.AppUserRepository;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(InitializationOrder.ReserveOwner)
public class AppUserDataInitializer extends DataInitializer<AppUser> {

	public AppUserDataInitializer(AppUserRepository repository) {
		super(AppUser.class, repository, "bootstrap/app-users.json");
	}

}

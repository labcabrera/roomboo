package org.lab.roomboo.api.bootstrap;

import java.util.List;

import org.lab.roomboo.api.bootstrap.DataInitializer.InitializationOrder;
import org.lab.roomboo.domain.model.ApiUser;
import org.lab.roomboo.domain.repository.ApiUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Order(InitializationOrder.ApiUser)
public class ApiUserDataInitializer extends DataInitializer<ApiUser> {

	@Autowired
	private PasswordEncoder passwordEncoder;

	public ApiUserDataInitializer(ApiUserRepository repository) {
		super(ApiUser.class, repository, "bootstrap/api-users.json");
	}

	@Override
	protected List<ApiUser> readEntities() {
		List<ApiUser> list = super.readEntities();
		list.stream().forEach(x -> x.setPassword(passwordEncoder.encode(x.getPassword())));
		return list;
	}
}

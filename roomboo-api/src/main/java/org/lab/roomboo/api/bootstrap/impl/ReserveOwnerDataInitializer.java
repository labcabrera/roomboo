package org.lab.roomboo.api.bootstrap.impl;

import org.lab.roomboo.api.bootstrap.DataInitializer;
import org.lab.roomboo.api.bootstrap.DataInitializer.InitializationOrder;
import org.lab.roomboo.domain.model.ReserveOwner;
import org.lab.roomboo.domain.repository.ReserveOwnerRepository;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(InitializationOrder.ReserveOwner)
public class ReserveOwnerDataInitializer extends DataInitializer<ReserveOwner> {

	public ReserveOwnerDataInitializer(ReserveOwnerRepository repository) {
		super(ReserveOwner.class, repository, "bootstrap/owners.json");
	}

}

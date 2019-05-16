package org.lab.roomboo.api.bootstrap;

import org.lab.roomboo.api.bootstrap.DataInitializer.InitializationOrder;
import org.lab.roomboo.domain.model.Reserve;
import org.lab.roomboo.domain.repository.ReserveRepository;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(InitializationOrder.RESERVE)
public class ReserveDataInitializer extends DataInitializer<Reserve> {

	public ReserveDataInitializer(ReserveRepository repository) {
		super(Reserve.class, repository, "bootstrap/reserves.json");
	}

}

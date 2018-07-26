package org.lab.roomboo.api.bootstrap;

import org.lab.roomboo.api.bootstrap.DataInitializer.InitializationOrder;
import org.lab.roomboo.domain.model.RoomGroup;
import org.lab.roomboo.domain.repository.BuildingRepository;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(InitializationOrder.Building)
public class RoomGroupDataInitializer extends DataInitializer<RoomGroup> {

	public RoomGroupDataInitializer(BuildingRepository repository) {
		super(RoomGroup.class, repository, "bootstrap/room-groups.json");
	}

}

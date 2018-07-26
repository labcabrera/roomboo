package org.lab.roomboo.api.bootstrap;

import org.lab.roomboo.api.bootstrap.DataInitializer.InitializationOrder;
import org.lab.roomboo.domain.model.RoomGroup;
import org.lab.roomboo.domain.repository.RoomGroupRepository;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(InitializationOrder.RoomGroup)
public class RoomGroupDataInitializer extends DataInitializer<RoomGroup> {

	public RoomGroupDataInitializer(RoomGroupRepository repository) {
		super(RoomGroup.class, repository, "bootstrap/room-groups.json");
	}

}

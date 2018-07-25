package org.lab.roomboo.api.bootstrap.impl;

import org.lab.roomboo.api.bootstrap.DataInitializer;
import org.lab.roomboo.api.bootstrap.DataInitializer.InitializationOrder;
import org.lab.roomboo.domain.model.Room;
import org.lab.roomboo.domain.repository.RoomRepository;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(InitializationOrder.Room)
public class RoomDataInitializer extends DataInitializer<Room> {

	public RoomDataInitializer(RoomRepository repository) {
		super(Room.class, repository, "bootstrap/rooms.json");
	}

}

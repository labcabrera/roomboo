package org.lab.roomboo.api.resource.assembler;

import org.lab.roomboo.api.controller.RoomController;
import org.lab.roomboo.api.resource.RoomResource;
import org.lab.roomboo.domain.model.Room;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class RoomResourceAssembler extends ResourceAssemblerSupport<Room, RoomResource> {

	public RoomResourceAssembler() {
		super(RoomController.class, RoomResource.class);
	}

	@Override
	public RoomResource toResource(Room reserve) {
		return new RoomResource(reserve);
	}

}

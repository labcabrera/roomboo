package org.lab.roomboo.api.resource.assembler;

import org.lab.roomboo.api.controller.RoomGroupController;
import org.lab.roomboo.api.resource.RoomGroupResource;
import org.lab.roomboo.domain.model.RoomGroup;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class RoomGroupResourceAssembler extends ResourceAssemblerSupport<RoomGroup, RoomGroupResource> {

	public RoomGroupResourceAssembler() {
		super(RoomGroupController.class, RoomGroupResource.class);
	}

	@Override
	public RoomGroupResource toResource(RoomGroup entity) {
		return new RoomGroupResource(entity);
	}

}

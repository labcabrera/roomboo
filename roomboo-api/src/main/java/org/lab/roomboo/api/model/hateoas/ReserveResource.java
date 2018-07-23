package org.lab.roomboo.api.model.hateoas;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.lab.roomboo.api.controller.BuildingController;
import org.lab.roomboo.api.controller.ReserveOwnerController;
import org.lab.roomboo.api.controller.RoomController;
import org.lab.roomboo.domain.model.Reserve;
import org.lab.roomboo.domain.model.ReserveOwner;
import org.lab.roomboo.domain.model.Room;
import org.springframework.hateoas.ResourceSupport;

import lombok.Getter;

@Getter
public class ReserveResource extends ResourceSupport {

	private final Reserve reserve;

	public ReserveResource(Reserve reserve) {
		this.reserve = reserve;
		String id = reserve.getId();
		add(linkTo(methodOn(BuildingController.class).findById(id)).withSelfRel());

		if (reserve.getRoom() != null) {
			String roomId = reserve.getRoom().getId();
			add(linkTo(methodOn(RoomController.class).findById(roomId)).withRel("room"));
			reserve.setRoom(Room.builder().id(roomId).build());
		}
		if (reserve.getOwner() != null) {
			String ownerId = reserve.getOwner().getId();
			add(linkTo(methodOn(ReserveOwnerController.class).findById(ownerId)).withRel("owner"));
			reserve.setOwner(ReserveOwner.builder().id(ownerId).build());
		}

	}

}

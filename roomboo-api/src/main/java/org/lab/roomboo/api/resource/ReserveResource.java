package org.lab.roomboo.api.resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.lab.roomboo.api.controller.AppUserController;
import org.lab.roomboo.api.controller.ReserveController;
import org.lab.roomboo.api.controller.RoomController;
import org.lab.roomboo.domain.model.AppUser;
import org.lab.roomboo.domain.model.Reserve;
import org.lab.roomboo.domain.model.Room;
import org.springframework.hateoas.ResourceSupport;

import lombok.Getter;

@Getter
public class ReserveResource extends ResourceSupport {

	private final Reserve reserve;

	public ReserveResource(Reserve reserve) {
		this.reserve = reserve;
		String id = reserve.getId();
		add(linkTo(methodOn(ReserveController.class).findById(id)).withSelfRel());

		if (reserve.getUser() != null) {
			String ownerId = reserve.getUser().getId();
			add(linkTo(methodOn(AppUserController.class).findById(ownerId)).withRel("owner"));
			reserve.setUser(AppUser.builder().id(ownerId).build());
		}

		if (reserve.getRoom() != null) {
			String roomId = reserve.getRoom().getId();
			add(linkTo(methodOn(RoomController.class).findById(roomId)).withRel("room"));
			reserve.setRoom(Room.builder().id(roomId).build());
		}

		add(linkTo(ReserveController.class).withRel("reserves"));
	}

}
